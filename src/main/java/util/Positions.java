package util;

import entity.Position;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Positions {

    private static final Map<Integer, InnerPosition> POSITION_MAP = new ConcurrentHashMap<>();

    public static void init(Collection<Position> positions) {
        POSITION_MAP.clear();
        putAll(positions);
    }

    public static void put(Position position) {
        POSITION_MAP.put(position.getId(), new InnerPosition(position));
    }

    public static void putAll(Collection<Position> positions) {
        if (positions != null && positions.size() > 0) {
            for (Position position : positions) {
                put(position);
            }
        }
    }

    public static void update(Position position) {
        if (position != null) {
            InnerPosition innerUser = POSITION_MAP.get(position.getId());
            Position cachePosition = innerUser.position;
            if (cachePosition != null) {
                synchronized (cachePosition) {
                    if (cachePosition.getId() != null) {
                        cachePosition.setId(cachePosition.getId());
                    }
                    if (cachePosition.getName() != null) {
                        cachePosition.setName(position.getName());
                        innerUser.formatPinyin();
                    }
                    cachePosition.setVisible(position.isVisible());
                }
            }
        }
    }

    public static void remove(int id) {
        POSITION_MAP.remove(id);
    }

    public static Position get(int id) {
        InnerPosition innerPosition = POSITION_MAP.get(id);
        return innerPosition == null ? null : (Position) innerPosition.position.clone();
    }

    public static List<Position> search(String searchKey) {
        searchKey = searchKey.toLowerCase(Locale.ROOT);
        Set<InnerPosition> searchPositions = new TreeSet<>(Comparator.comparing(o -> o.fullPinyin));
        Collection<InnerPosition> positions = POSITION_MAP.values();
        if (util.StringUtils.isBlank(searchKey)) {
            searchPositions.addAll(positions);
        } else {
            for (InnerPosition position : positions) {
                if (position.position.getName().contains(searchKey) || position.sortPinyin.contains(searchKey) || position.fullPinyin.contains(searchKey)) {
                    searchPositions.add(position);
                }
            }
        }
        List<Position> result = new ArrayList<>();
        for (InnerPosition innerPosition : searchPositions) {
            result.add((Position) innerPosition.position.clone());
        }
        return result;
    }

    private static class InnerPosition {

        Position position;
        String sortPinyin;
        String fullPinyin;

        InnerPosition(Position position) {
            this.position = position;
            formatPinyin();
        }

        void formatPinyin() {
            this.sortPinyin = PinyinTools.getFirstSpell(position.getName()).toLowerCase(Locale.ROOT);
            this.fullPinyin = PinyinTools.getFullSpell(position.getName()).toLowerCase(Locale.ROOT);
        }
    }
}
