package net.fabricmc.example.listeners;

import net.minecraft.text.Text;

public interface ChatListener {
    public void processMessage(Text message);
}
