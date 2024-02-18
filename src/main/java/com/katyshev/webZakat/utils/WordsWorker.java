package com.katyshev.webZakat.utils;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class WordsWorker {
    private static final String[] manufacturers;

    static  {
        manufacturers = new String[] {
                "верт", "Верт", "ВЕРТ", "Гексал", "ГЕКСАЛ",
                "канон", "Канон", "КАНОН", "прана", "ПРАНА"
        };
    }

    public String generateSearchQuery(String line) {
        StringBuilder result = new StringBuilder();
        String[] array = line.split(" ");
        ArrayList<String> list = new ArrayList<>(Arrays.asList(array));


        // вычисление и удаление лекарстевнной формы
        int indexOfForm = -1;
        String lecForm = null;
        for (String s : list) {
            if ((lecForm = extractLecForm(s)) != null) {
                indexOfForm = list.indexOf(s);
                break;
            }
        }
        if (indexOfForm > 0) {
            list.remove(indexOfForm);
        }


        // вычисление и удаление количества
        int indexOfCount = -1;
        String count = null;
        for (String s: list) {
            if ((count = extractCount(s)) != null) {
                indexOfCount = list.indexOf(s);
                break;
            }
        }
        if (indexOfCount > 0) {
            list.remove(indexOfCount);
        }


        // вычисление производителя
        String manufacturer = null;
        String newName = null;
        int indexInList = 0;
        for (int i = 0; i < list.size(); i++) {
            String word = list.get(i);
            for (int j = 0; j < manufacturers.length; j++) {
                if (word.contains(manufacturers[j])) {
                    int enterIndex = word.indexOf(manufacturers[j]);
                    manufacturer = manufacturers[j];
                    String oldname = word;
                    newName = word.substring(0, enterIndex-1);
                    Collections.replaceAll(list, oldname, newName);
                    break;
                }
            }
        }


        // вычисление дозировки
        int indexOfDose = -1;
        String dose = null;
        for (String s: list) {
            if ((dose = extractDose(s)) != null) {
                indexOfDose = list.indexOf(s);
                break;
            }
        }
        if (indexOfDose > 0) {
            list.remove(indexOfDose);
        }


        // вычисление имени ()
        List<String> words = list.stream()
                .filter(x -> x.length() >= 4)
                .collect(Collectors.toList());
        for (String str : words) {
            if (str.length() <= 6) {
                result.append(str + " ");
            } else {
                int length = (int)(str.length() * 0.85);
                result.append((str.substring(0, length)) + " ");
            }
        }

        if (dose != null) {
            result.append(dose + " ");
        }

        if (count != null) {
            result.append(count + " ");
        }

        if (lecForm != null) {
            result.append(lecForm + " ");
        }

        if (manufacturer != null) {
            result.append(manufacturer + " ");
        }

        return result.toString().replaceAll("\\s+", " ");
    }

    public String extractLecForm(String s) {
        String lecForm = null;

        String findForm = s.replaceAll("[.,\\-_=+*]", "");
        if (findForm.equalsIgnoreCase("таб") ||
                findForm.equalsIgnoreCase("табл") ||
                findForm.equalsIgnoreCase("таблет") ||
                findForm.equalsIgnoreCase("таблетки") ||
                findForm.equalsIgnoreCase("тб")) {
            lecForm = "т";

        } else if (findForm.equalsIgnoreCase("капс") ||
                findForm.equalsIgnoreCase("капсул") ||
                findForm.equalsIgnoreCase("капсулы")) {
            lecForm = "капс";

        } else if (findForm.equalsIgnoreCase("маз") ||
                findForm.equalsIgnoreCase("мазь")) {
            lecForm = "маз";

        } else if (findForm.equalsIgnoreCase("крем")) {
            lecForm = "крем";

        } else if (findForm.equalsIgnoreCase("гель")) {
            lecForm = "гель";

        } else if (findForm.equalsIgnoreCase("супп") ||
                findForm.equalsIgnoreCase("св") ||
                findForm.equalsIgnoreCase("суппозитории") ||
                findForm.equalsIgnoreCase("свечи")) {
            lecForm = "супп";

        } else if (findForm.equalsIgnoreCase("сир") ||
                findForm.equalsIgnoreCase("сироп")) {
            lecForm = "сир";

        } else if (findForm.equalsIgnoreCase("сусп") ||
                findForm.equalsIgnoreCase("суспензия")) {
            lecForm = "сусп";

        } else if (findForm.equalsIgnoreCase("амп") ||
                findForm.equalsIgnoreCase("ампулы")) {
            lecForm = "амп";

        } else if (findForm.equalsIgnoreCase("пл") ||
                findForm.equalsIgnoreCase("пласт") ||
                findForm.equalsIgnoreCase("пластырь")) {
            lecForm = "пласт";

        } else if (findForm.equalsIgnoreCase("капли") ||
                findForm.equalsIgnoreCase("кап") ||
                findForm.equalsIgnoreCase("капл")) {
            lecForm = "кап";

        } else if (findForm.equalsIgnoreCase("паст") ||
                findForm.equalsIgnoreCase("паста")) {
            lecForm = "паст";

        } else if (findForm.equalsIgnoreCase("спрей") ||
                findForm.equalsIgnoreCase("спр")) {
            lecForm = "спр";

        } else if (findForm.equalsIgnoreCase("шампунь") ||
                findForm.equalsIgnoreCase("шамп")) {
            lecForm = "шамп";

        } else if (findForm.equalsIgnoreCase("назал") ||
                findForm.equalsIgnoreCase("назальный")) {
            lecForm = "наз";

        }

        return lecForm;
    }

    public String extractCount(String s) {
        String count = null;
        String findCount = s.replaceAll("[.\\-_=+*]", "");
        if (findCount.matches(".*\\d.*")) {
            if (findCount.startsWith("№") || findCount.startsWith("n") || findCount.startsWith("х")) {
                count = findCount.replaceAll("\\D", " ").trim();
            } else if (findCount.endsWith(",0")) {
                count = findCount.substring(0, (findCount.length()-2));
            }
        }

        return count;
    }

    public String extractDose(String s) {
        String dose = null;
        String findDose = s.replaceAll("(,|\\.|\\*|/)$", "");
        if (findDose.matches(".*\\d.*")) {
            if (findDose.endsWith("мг")) {
                dose = findDose.replaceAll("\\D", "").replaceAll("(^0+)?(0+$)?", "");
            } else if (findDose.matches("\\d*мл")) {
                dose = findDose.replaceAll("мл", "");
            } else if (findDose.matches("\\d+")) {
                dose = findDose.replaceAll("0+$", "");
            } else {
                dose = findDose.replaceAll("[^1-9]", " ");
            }
        }

        return dose;
    }

    public String formatRequest(String request) {
        return Arrays.stream(request.split(" "))
                .distinct()
                .collect(Collectors.joining(" "))
                .toLowerCase(Locale.ROOT);
    }
}
