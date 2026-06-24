package numenology.nodes;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import numenology.block.ModBlockEntities;
import numenology.energy.NumenAspect;
import numenology.energy.ModAspects;

import java.util.HashMap;
import java.util.Map;

public class NumenNodeBlockEntity extends BlockEntity {

    // ==============================
    // 📦 ДАННЫЕ УЗЛА
    // ==============================
    private final Map<NumenAspect, Integer> aspects = new HashMap<>();
    private int capacity = 100; // Ёмкость для чистого Нумена
    private NodeType nodeType = NodeType.NORMAL; // Текущий тип узла

    public NumenNodeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.NUMEN_NODE, pos, state);

        // СТРАХОВКА ДЛЯ ДЕБАГ-КОМАНДЫ:
        // Теперь при спавне через /numen node мапа не будет пустой!
        this.aspects.put(ModAspects.NUMEN, this.capacity);
    }

    // ==============================
    // 🔧 ВАРИАТИВНАЯ ИНИЦИАЛИЗАЦИЯ
    // ==============================
    public void initialize(NodeType type, Map<NumenAspect, Integer> inputAspects, int capacity) {
        this.nodeType = type;
        this.capacity = capacity;
        this.aspects.clear();
        this.aspects.putAll(inputAspects);
        this.markDirty();
    }

    // ==============================
    // ⚡ ЛОГИКА ТИКА (РАБОТА УЗЛА)
    // ==============================
    public static void tick(World world, BlockPos pos, BlockState state, NumenNodeBlockEntity node) {
        if (world.isClient) return;

        // Каждые 40 тиков (2 секунды) узел взаимодействует с миром
        if (world.getTime() % 40 == 0) {
            ServerWorld serverWorld = (ServerWorld) world;

            // Динамически получаем данные чанка, в котором находится узел
            var worldState = numenology.block.NumenWorldState.get(serverWorld);
            var chunkPos = new net.minecraft.util.math.ChunkPos(pos);
            var chunkData = worldState.getOrCreateChunkData(chunkPos.x, chunkPos.z);
            var managerChunk = numenology.energy.NumenEnergyManager.getChunk(serverWorld, chunkPos);

            // 🌀 УНИВЕРСАЛЬНЫЙ СУПЕР-ТИК ДЛЯ ВСЕХ АСПЕКТОВ:
            // Берем ВСЕ аспекты, находящиеся внутри узла, и подпитываем ими окружающий чанк
// 🌀 УНИВЕРСАЛЬНЫЙ ТИК ДЛЯ ВСЕХ АСПЕКТОВ:
            for (Map.Entry<NumenAspect, Integer> entry : node.aspects.entrySet()) {
                NumenAspect aspect = entry.getKey();
                int amount = entry.getValue();

                if (amount <= 0) continue;

                // 1. Базовая энергия NUMEN
                if (aspect == ModAspects.NUMEN) {
                    if (managerChunk.getEnergy() < chunkData.getMaxEnergy()) {
                        managerChunk.addEnergy(5);
                    }
                }
                // 2. Заражение INFUNGUM (Миазмы растут, стабильность падает)
                else if (aspect == ModAspects.INFUNGUM) {
                    // Растим миазмы, если они ещё не на максимуме
                    if (managerChunk.getMiasma() < 100) {
                        managerChunk.addMiasma(1);
                    }
                    // 🔥 ДОБАВЛЕНО: Пропорционально бьём по стабильности чанка!
                    if (managerChunk.getStability() > 0) {
                        managerChunk.addStability(-1); // Передаём отрицательное значение, чтобы уменьшить
                    }
                }
                // 3. Свет LUMEN подпитывает стабильность чанка
                else if (aspect == ModAspects.LUMEN) {
                    if (managerChunk.getStability() < 100) {
                        managerChunk.addStability(1);
                    }
                }
                // (Сюда же в будущем пойдут блоки для UMBRA, POTENTIA, VITA и MECHA)
            }

            node.markDirty();
        }
    }

    public NodeType getNodeType() {
        return this.nodeType;
    }

    // ==============================
    // 🔍 МЕТОДЫ ДЛЯ НУМЕНОМЕТРА
    // ==============================
    public int getEnergy(NumenAspect aspect) {
        return aspects.getOrDefault(aspect, 0);
    }

    public int getPower() {
        return this.capacity;
    }

    public Map<NumenAspect, Integer> getAllAspects() {
        return this.aspects;
    }

    // Отправляет данные на клиент при загрузке чанка
    @Override
    public net.minecraft.network.packet.Packet<net.minecraft.network.listener.ClientPlayPacketListener> toUpdatePacket() {
        return net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket.create(this);
    }

    // Собирает NBT для отправки клиенту
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    // Метод для вызова синхронизации (вызывай его в tick() или при изменении аспектов)
    public void markDirtyAndSync() {
        this.markDirty();
        if (this.world != null && !this.world.isClient) {
            ((net.minecraft.server.world.ServerWorld) this.world).getChunkManager().markForUpdate(this.pos);
        }
    }

    // ==============================
    // 💾 СИНХРОНИЗАЦИЯ И ХРАНЕНИЕ (NBT)
    // ==============================
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("capacity", capacity);
        nbt.putString("NodeType", nodeType.getId());

        NbtCompound aspectsTag = new NbtCompound();
        for (Map.Entry<NumenAspect, Integer> entry : aspects.entrySet()) {
            aspectsTag.putInt(entry.getKey().getId(), Math.max(0, entry.getValue()));
        }
        nbt.put("aspects", aspectsTag);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.capacity = Math.max(1, nbt.getInt("capacity"));

        // Читаем тип узла
        String typeId = nbt.getString("NodeType");
        this.nodeType = NodeType.NORMAL;
        for (NodeType type : NodeType.values()) {
            if (type.getId().equalsIgnoreCase(typeId)) {
                this.nodeType = type;
                break;
            }
        }

        // Читаем аспекты
        this.aspects.clear();
        NbtCompound aspectsTag = nbt.getCompound("aspects");

        // Загружаем NUMEN
        if (aspectsTag.contains(ModAspects.NUMEN.getId())) {
            this.aspects.put(ModAspects.NUMEN, aspectsTag.getInt(ModAspects.NUMEN.getId()));
        } else {
            this.aspects.put(ModAspects.NUMEN, this.capacity);
        }

        // Загружаем все остальные кастомные аспекты по их ID из регистра
        checkAndLoadAspect(aspectsTag, ModAspects.LUMEN);
        checkAndLoadAspect(aspectsTag, ModAspects.UMBRA);
        checkAndLoadAspect(aspectsTag, ModAspects.POTENTIA);
        checkAndLoadAspect(aspectsTag, ModAspects.VITA);
        checkAndLoadAspect(aspectsTag, ModAspects.INFUNGUM);
        checkAndLoadAspect(aspectsTag, ModAspects.MECHA);
        checkAndLoadAspect(aspectsTag, ModAspects.ANTIQUUS);
    }

    private void checkAndLoadAspect(NbtCompound tag, NumenAspect aspect) {
        if (tag.contains(aspect.getId())) {
            int val = tag.getInt(aspect.getId());
            if (val > 0) {
                this.aspects.put(aspect, val);
            }
        }
    }
}