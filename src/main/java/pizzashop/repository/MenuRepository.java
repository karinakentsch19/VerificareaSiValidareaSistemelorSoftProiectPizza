package pizzashop.repository;

import pizzashop.model.MenuDataModel;
import pizzashop.utilities.FileLogger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class MenuRepository {
    private static String filename = "data/menu.txt";
    private List<MenuDataModel> listMenu;
    private FileLogger fileLogger ;

    public MenuRepository() {
        fileLogger = new FileLogger("app.log");
    }

    private void readMenu() {
        File file = new File(filename);
        this.listMenu = new ArrayList();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = br.readLine()) != null) {
                MenuDataModel menuItem = getMenuItem(line);
                if (menuItem != null)
                    listMenu.add(menuItem);
            }
            br.close();
        } catch (FileNotFoundException e) {
            fileLogger.error("Menu file not found.");
            e.printStackTrace();
        } catch (IOException e) {
            fileLogger.error("Error occurs while reading menu file.");
            e.printStackTrace();
        }
    }

    private MenuDataModel getMenuItem(String line) {
        if (line != null && line.matches("[a-zA-Z ]+,\\d+(\\.\\d+)?")) {
            StringTokenizer st = new StringTokenizer(line, ",");
            String name = st.nextToken();
            double price = Double.parseDouble(st.nextToken());
            return new MenuDataModel(name, 0, price);
        } else {
            fileLogger.error("Bad formatted line in menu file: " + line);
            return null;
        }
    }

    public List<MenuDataModel> getMenu() {
        readMenu();//create a new menu for each table, on request
        return listMenu;
    }
}
