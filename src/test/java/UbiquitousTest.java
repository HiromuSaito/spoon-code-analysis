import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;
import spoon.Launcher;
import spoon.SpoonAPI;
import spoon.reflect.declaration.CtType;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class UbiquitousTest {
    @Test
    void ubiquitousTest() throws Exception {
        List<String> ubiquitousList = getUbiquitousList();
        SpoonAPI spoon = new Launcher();
        spoon.addInputResource("src/main/java/org/example/domain");
        spoon.buildModel();

        List<CtType<?>> notUbiquitousList = spoon
                .getModel()
                .getAllTypes()
                .stream()
                .filter(type -> {
                    for (String ubiquitous : ubiquitousList) {
                        if (ubiquitous.equalsIgnoreCase(type.getSimpleName())) return false;
                    }
                    return true;
                })
                .toList();

        notUbiquitousList.forEach(t -> System.out.println(t.getQualifiedName() + " is not included ubiquitousList"));
        assertTrue(notUbiquitousList.isEmpty());
    }

    private List<String> getUbiquitousList() throws Exception {
        ClassLoader classLoader = this.getClass().getClassLoader();
        URL resouce = classLoader.getResource("ubiquitous.xlsx");
        File file = new File(resouce.getPath());
        Workbook workbook = WorkbookFactory.create(new FileInputStream(file));
        Sheet sheet = workbook.getSheetAt(0);
        List<String> ubiquitousList = new ArrayList<>();
        for (Row row : sheet) {
            ubiquitousList.add(row.getCell(0).getStringCellValue());
        }
        return ubiquitousList;
    }
}
