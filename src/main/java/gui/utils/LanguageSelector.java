package gui.utils;

import javafx.scene.control.ComboBox;

import java.util.Locale;

public class LanguageSelector {
    public static ComboBox<String> createLanguageComboBox(Runnable onLanguageChange) {
        ComboBox<String> languageCombo = new ComboBox<>();
        languageCombo.getItems().addAll("Русский", "English", "Netherland", "Swedish");
        switch (LocaleManager.getCurrentLocale().getLanguage()) {
            case "en":
                languageCombo.setValue("English");
                break;
            case "nl":
                languageCombo.setValue("Netherland");
                break;
            case "swe":
                languageCombo.setValue("Swedish");
                break;
            default:
                languageCombo.setValue("Русский");
        }
        languageCombo.setOnAction(event -> {
            String selected = languageCombo.getValue();
            switch (selected) {
                case "English":
                    Locale.setDefault(Locale.ENGLISH);
                    break;
                case "Netherland":
                    Locale.setDefault(new Locale("nl"));
                    break;
                case "Swedish":
                    Locale.setDefault(new Locale("swe"));
                    break;
                default:
                    Locale.setDefault(new Locale("ru"));
            }
            //System.out.println(selected);
            onLanguageChange.run();
        });

        return languageCombo;
    }

    private static void refreshUI() {
        // Этот метод нужно реализовать в вашем главном классе
        // Он должен перезагрузить все текстовые элементы
    }
}