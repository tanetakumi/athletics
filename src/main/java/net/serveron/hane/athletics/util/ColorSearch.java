package net.serveron.hane.athletics.util;

import net.kyori.adventure.text.format.TextColor;

public class ColorSearch {
    public static final TextColor Black = TextColor.color(0,0,0);
    public static final TextColor DarkBlue = TextColor.color(0,0,170);
    public static final TextColor DarkGreen = TextColor.color(0,170,0);
    public static final TextColor DarkAqua = TextColor.color(0,170,170);
    public static final TextColor DarkRed = TextColor.color(170,0,0);
    public static final TextColor DarkPurple = TextColor.color(170,0,170);
    public static final TextColor Gold = TextColor.color(255,170,0);
    public static final TextColor Gray = TextColor.color(170,170,170);
    public static final TextColor DarkGray = TextColor.color(85,85,85);
    public static final TextColor Blue = TextColor.color(85,85,255);
    public static final TextColor Green = TextColor.color(85,255,85);
    public static final TextColor Aqua = TextColor.color(85,255,255);
    public static final TextColor Red = TextColor.color(255,85,85);
    public static final TextColor LightPurple = TextColor.color(255,85,255);
    public static final TextColor Yellow = TextColor.color(255,255,85);
    public static final TextColor White = TextColor.color(255,255,255);

    public static TextColor stringToColor(String text){
        switch (text.toUpperCase()){
            case "BLACK":
                return TextColor.color(0,0,0);
            case "DARKBLUE":
                return TextColor.color(0,0,170);
            case "DARKGREEN":
                return TextColor.color(0,170,0);
            case "DARKAQUA":
                return TextColor.color(0,170,170);
            case "DARKRED":
                return TextColor.color(170,0,0);
            case "DARKPURPLE":
                return TextColor.color(170,0,170);
            case "GOLD":
                return TextColor.color(255,170,0);
            case "GRAY":
                return TextColor.color(170,170,170);
            case "DARKGRAY":
                return TextColor.color(85,85,85);
            case "BLUE":
                return TextColor.color(85,85,255);
            case "GREEN":
                return TextColor.color(85,255,85);
            case "AQUA":
                return TextColor.color(85,255,255);
            case "RED":
                return TextColor.color(255,85,85);
            case "LIGHTPURPLE":
                return TextColor.color(255,85,255);
            case "YELLOW":
                return TextColor.color(255,255,85);
            default:
                return TextColor.color(255,255,255);

        }
    }
}
