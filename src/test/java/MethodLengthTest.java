import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class MethodLengthTest {
    private static final int MAX_METHOD_LENGTH = 50;

    @Test
    void testMethodLength() {
        SpoonAPI spoon = new Launcher();
        spoon.addInputResource("src/main/java/org/example");
        spoon.buildModel();

        Map<String, Map<String, Integer>> results = new HashMap<>();

        for (CtType<?> type : spoon.getModel().getAllTypes()) {
            Map<String, Integer> overLengthMethods = new HashMap<>();
            for (CtMethod method : type.getMethods()) {
                //メソッドの行数を算出
                int length = method.getOriginalSourceFragment().getSourceCode().split("\r\n|\r|\n").length;
                if (length > MAX_METHOD_LENGTH) {
                    overLengthMethods.put(method.getSimpleName(), length);
                }
            }
            if (!overLengthMethods.isEmpty()) {
                results.put(type.getQualifiedName(), overLengthMethods);
            }

        }
        for (Map.Entry<String, Map<String, Integer>> result : results.entrySet()) {
            System.out.println(result.getKey() + " has over length methods");
            result.getValue().forEach((key, value) -> System.out.println("\t " + key + " length is " + value));
        }
        assertTrue(results.isEmpty());
    }
}
