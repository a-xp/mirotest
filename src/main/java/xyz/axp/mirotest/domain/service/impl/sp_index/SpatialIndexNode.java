package xyz.axp.mirotest.domain.service.impl.sp_index;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import xyz.axp.mirotest.domain.entities.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class SpatialIndexNode {
    private Map<Integer, SpatialIndexNode> children;
    private List<Widget> objects;

    public static SpatialIndexNode empty() {
        return new SpatialIndexNode(null, null);
    }

    public void addEntry(int lvl, int[] path, Widget value) {
        if (objects == null) {
            objects = new ArrayList<>();
        }
        objects.add(value);
        if (lvl < path.length) {
            if (children == null) {
                children = new HashMap<>();
            }
            SpatialIndexNode child = children.computeIfAbsent(path[lvl], i -> empty());
            child.addEntry(lvl + 1, path, value);
        }
    }

    public List<Widget> searchSector(int lvl, int[] path) {
        if (lvl < path.length) {
            int id = path[lvl];
            if (children == null || !children.containsKey(id)) {
                return List.of();
            }
            return children.get(id).searchSector(lvl + 1, path);
        } else {
            return objects == null ? List.of() : objects;
        }
    }

}
