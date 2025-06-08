package com.example.gamequeue.data.model;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

// Used to pass data between fragments
public class MainSharedViewModel extends ViewModel {
    private final ArrayList<ConsoleModel> consoleList = new ArrayList<>();

    public void fetchSetup() {
        // Setup logic to fetch ONLY ONCE to avoid re-fetching
        // Though i doubt we'll be using this since Firebase
        // Already have a realtime database listener built-in
        // But this is good for dummy data for now

        consoleList.clear();
        consoleList.add(new ConsoleModel("XBOX", 0, "Day, DD MM YY", "Time Timezone", "XBOX X Series", "Gigabyte Curved 120Hz", "Wireless Dualsense Stick"));
        consoleList.add(new ConsoleModel("Playstation 5", 1, "Day, DD MM YY", "Time Timezone", "Playstation 5 Pro", "2K HDR AMOLED 120Hz", "PS Pro Limited Pad"));
        consoleList.add(new ConsoleModel("Desktop PC", 2, "Day, DD MM YY", "Time Timezone", "Alienware X", "4K HDR Monitor 240Hz", "Razer Viper"));
        consoleList.add(new ConsoleModel("Nintendo Switch", 0, "Fri, 31 May 24", "09:00 AM JST", "Nintendo Switch OLED", "Built-in 7-inch OLED", "Joy-Con Pair (Neon Red/Blue)"));
        consoleList.add(new ConsoleModel("Steam Deck", 1, "Sat, 01 Jun 24", "11:45 AM CET", "Steam Deck 512GB", "7-inch LCD 60Hz", "Integrated Controls"));
        consoleList.add(new ConsoleModel("ASUS ROG Ally", 2, "Sun, 02 Jun 24", "03:20 PM PST", "ASUS ROG Ally Z1 Extreme", "7-inch 1080p 120Hz IPS", "Integrated ROG Gaming Controls"));
        consoleList.add(new ConsoleModel("Playstation Portal", 0, "Mon, 03 Jun 24", "08:00 AM EST", "Playstation Portal Remote Player", "8-inch LCD 1080p 60Hz", "DualSense-like Controls"));
        consoleList.add(new ConsoleModel("Gaming Laptop", 1, "Tue, 04 Jun 24", "06:50 PM GMT", "Razer Blade 16", "16-inch QHD+ 240Hz Mini-LED", "Built-in Keyboard & Trackpad + Razer Naga Mouse"));
        consoleList.add(new ConsoleModel("Retro Handheld", 2, "Wed, 05 Jun 24", "10:30 AM CST", "Anbernic RG35XX", "3.5-inch IPS Display", "D-pad and Action Buttons"));
        consoleList.add(new ConsoleModel("Xbox Series S", 0, "Thu, 06 Jun 24", "01:15 PM EST", "Xbox Series S - 1TB Carbon Black", "LG UltraGear 1440p 144Hz", "Xbox Wireless Controller (Carbon Black)"));
        consoleList.add(new ConsoleModel("Custom Built PC", 1, "Fri, 07 Jun 24", "04:00 PM PST", "AMD Ryzen 9 + NVIDIA RTX 4080", "Samsung Odyssey G9 49-inch 240Hz", "Corsair K95 Keyboard & Logitech G Pro X Superlight"));
    }

    public ArrayList<ConsoleModel> getConsoleList() {
        return consoleList;
    }
}
