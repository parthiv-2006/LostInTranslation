package translation;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.*;


public class GUI {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JPanel countryPanel = new JPanel();
            countryPanel.setLayout(new GridLayout(0, 1));

            Translator translator = new JSONTranslator();
            CountryCodeConverter countryCodeConverter = new CountryCodeConverter();
            LanguageCodeConverter languageCodeConverter = new LanguageCodeConverter();

            String[] countries = new String[translator.getCountryCodes().size()];
            int i = 0;
            for (String countryCode : translator.getCountryCodes()) {
                countries[i++] = countryCodeConverter.fromCountryCode(countryCode);
            }


            // create the JList with the array of strings and set it to allow multiple
            // items to be selected at once.
            JList<String> list = new JList<>(countries);
            list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

            // place the JList in a scroll pane so that it is scrollable in the UI
            JScrollPane scrollPane = new JScrollPane(list);
            countryPanel.add(scrollPane);


            JPanel languagePanel = new JPanel();
            languagePanel.add(new JLabel("Language:"));
            JComboBox<String> languageComboBox = new JComboBox<>();
            for(String languageCode : translator.getLanguageCodes()) {
                languageComboBox.addItem(languageCodeConverter.fromLanguageCode(languageCode));
            }
            languagePanel.add(languageComboBox);


            JPanel resultPanel = new JPanel();
            resultPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            JLabel resultLabelText = new JLabel("Translation:");
            resultPanel.add(resultLabelText);
            JLabel resultLabel = new JLabel("\t\t\t\t\t\t\t");
            resultPanel.add(resultLabel);


            list.addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    if (!e.getValueIsAdjusting()) {
                        if (languageComboBox.getSelectedItem() == null) return;

                        String language = languageCodeConverter.fromLanguage((String) languageComboBox.getSelectedItem());

                        int index = list.getSelectedIndex();
                        String countryCode = translator.getCountryCodes().get(index);

                        Translator translator = new JSONTranslator();

                        String result = translator.translate(countryCode, language);
                        if (result == null) {
                            result = "no translation found!";
                        }
                        resultLabel.setText(result);
                    }
                }
            });

            languageComboBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (languageComboBox.getSelectedItem() == null) return;

                    String language = languageCodeConverter.fromLanguage((String) languageComboBox.getSelectedItem());

                    int index = list.getSelectedIndex();
                    String countryCode = translator.getCountryCodes().get(index);

                    Translator translator = new JSONTranslator();

                    String result = translator.translate(countryCode, language);
                    if (result == null) {
                        result = "no translation found!";
                    }
                    resultLabel.setText(result);
                }
            });



            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            mainPanel.add(languagePanel);
            mainPanel.add(resultPanel);
            mainPanel.add(countryPanel);


            JFrame frame = new JFrame("Country Name Translator");
            frame.setContentPane(mainPanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
}
