package com.example.gamequeue.data.model;

import java.util.ArrayList;
import java.util.stream.Collectors;

// Used to pass data between activity & fragments
public class ConsoleSharedViewModel {
    private static final ArrayList<ConsoleModel> consoleList;

    static {
        consoleList = new ArrayList<>();
        consoleList.clear();
        consoleList.add(new ConsoleModel(1, "XBOX", 0, "Day, DD MM YY", "Time Timezone", "XBOX X Series", "Gigabyte Curved 120Hz", "Wireless Dualsense Stick"));
        consoleList.add(new ConsoleModel(2, "Playstation 5", 1, "Day, DD MM YY", "Time Timezone", "Playstation 5 Pro", "2K HDR AMOLED 120Hz", "PS Pro Limited Pad"));
        consoleList.add(new ConsoleModel(3, "Desktop PC", 2, "Day, DD MM YY", "Time Timezone", "Alienware X", "4K HDR Monitor 240Hz", "Razer Viper"));
        consoleList.add(new ConsoleModel(4, "Nintendo Switch", 0, "Fri, 31 May 24", "09:00 AM JST", "Nintendo Switch OLED", "Built-in 7-inch OLED", "Joy-Con Pair (Neon Red/Blue)"));
        consoleList.add(new ConsoleModel(5, "Steam Deck", 1, "Sat, 01 Jun 24", "11:45 AM CET", "Steam Deck 512GB", "7-inch LCD 60Hz", "Integrated Controls"));
        consoleList.add(new ConsoleModel(6, "ASUS ROG Ally", 2, "Sun, 02 Jun 24", "03:20 PM PST", "ASUS ROG Ally Z1 Extreme", "7-inch 1080p 120Hz IPS", "Integrated ROG Gaming Controls"));
        consoleList.add(new ConsoleModel(7, "Playstation Portal", 0, "Mon, 03 Jun 24", "08:00 AM EST", "Playstation Portal Remote Player", "8-inch LCD 1080p 60Hz", "DualSense-like Controls"));
        consoleList.add(new ConsoleModel(8, "Gaming Laptop", 1, "Tue, 04 Jun 24", "06:50 PM GMT", "Razer Blade 16", "16-inch QHD+ 240Hz Mini-LED", "Built-in Keyboard & Trackpad + Razer Naga Mouse"));
        consoleList.add(new ConsoleModel(9, "Retro Handheld", 2, "Wed, 05 Jun 24", "10:30 AM CST", "Anbernic RG35XX", "3.5-inch IPS Display", "D-pad and Action Buttons"));
        consoleList.add(new ConsoleModel(10, "Xbox Series S", 0, "Thu, 06 Jun 24", "01:15 PM EST", "Xbox Series S - 1TB Carbon Black", "LG UltraGear 1440p 144Hz", "Xbox Wireless Controller (Carbon Black)"));
        consoleList.add(new ConsoleModel(11,"Custom Built PC", 1, "Fri, 07 Jun 24", "04:00 PM PST", "AMD Ryzen 9 + NVIDIA RTX 4080", "Samsung Odyssey G9 49-inch 240Hz", "Corsair K95 Keyboard & Logitech G Pro X Superlight"));
    }

    public static ArrayList<ConsoleModel> getConsoleList() {
        return consoleList;
    }

    public static ArrayList<ConsoleModel> getPendingConsoleList() {
        return consoleList.stream().filter(consoleModel -> consoleModel.getRawStatus() == 0).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConsoleModel> getCompletedConsoleList() {
        return consoleList.stream().filter(consoleModel -> consoleModel.getRawStatus() == 1).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConsoleModel> getCanceledConsoleList() {
        return consoleList.stream().filter(consoleModel -> consoleModel.getRawStatus() == 2).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConsoleModel> getFilteredWordList(String[] word) {
        // Filter by word
        return consoleList.stream().filter(consoleModel -> consoleModel.getTitle().toLowerCase().contains(word[0].toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<ConsoleModel> getFilteredStatusWordList(String[] word, int status) {
        // Filter by status first
        ArrayList<ConsoleModel> filteredList = new ArrayList<>();

        // Check Status
        if (status == -1) {
            filteredList.addAll(consoleList);
        } else {
            filteredList.addAll(consoleList.stream().filter(consoleModel -> consoleModel.getRawStatus() == status).collect(Collectors.toCollection(ArrayList::new)));
        }

        // Filter by word
        return filteredList.stream().filter(consoleModel -> consoleModel.getTitle().toLowerCase().contains(word[0].toLowerCase())).collect(Collectors.toCollection(ArrayList::new));
    }
}
