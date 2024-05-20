import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.CtModel;
import spoon.reflect.declaration.CtTypeInformation;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class FieldCountTest {
    private static final int MAX_FIELD_COUNT = 10;

    @Test
    void testFiledCount() {
        SpoonAPI spoon = new Launcher();
        spoon.addInputResource("src/main/java/org/example");
        CtModel model = spoon.buildModel();

        Map<String, Integer> classToFieldCnt = model.getAllTypes().stream()
                .filter(t -> t.getFields().size() > MAX_FIELD_COUNT)
                .collect(Collectors.toMap(CtTypeInformation::getQualifiedName, t -> t.getFields().size()));
        classToFieldCnt.forEach((key, value) -> System.out.println(key + " has " + value + " fields"));
        assertTrue(classToFieldCnt.isEmpty());
    }
}
