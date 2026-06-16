package view;

import Model.IrrigationSystem;
import Model.Plant;
import Model.SoilLayer;
import Model.SoilProfile;
import Model.Weather;
import dao.PlantDAO;
import exception.UmidadeInvalidaException;
import service.IrrigationRecommendation;
import service.IrrigationService;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

/**
 * Interface principal com CRUD de plantas e simulação de irrigação.
 */
public class TelaPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;

    private final PlantDAO plantDAO;
    private final IrrigationService irrigationService = new IrrigationService();

    private final JTextField nomeField = new JTextField(20);
    private final JTextField tipoField = new JTextField(20);
    private final JTextField necessidadeField = new JTextField(10);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] { "ID", "Nome", "Tipo", "Necessidade hídrica (L)" }, 0) {
        private static final long serialVersionUID = 1L;

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable plantTable = new JTable(tableModel);
    private Integer selectedPlantId;

    private final JComboBox<Plant> plantCombo = new JComboBox<>();
    private final JTextField temperaturaField = new JTextField("30", 8);
    private final JTextField umidadeArField = new JTextField("70", 8);
    private final JTextField precipitacaoField = new JTextField("0", 8);
    private final JTextField profundidadeField = new JTextField("20", 8);
    private final JTextField umidadeSoloField = new JTextField("45", 8);
    private final JTextField retencaoField = new JTextField("90", 8);
    private final JTextField horarioField = new JTextField("08:00", 8);
    private final JTextArea resultadoArea = new JTextArea();
    private IrrigationSystem currentIrrigation;

    public TelaPrincipal(PlantDAO plantDAO) {
        if (plantDAO == null) {
            throw new IllegalArgumentException("O DAO de plantas é obrigatório.");
        }
        this.plantDAO = plantDAO;
        configurarJanela();
        construirInterface();
        carregarPlantas();
    }

    private void configurarJanela() {
        setTitle("Sistema de Irrigação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(920, 620));
        setSize(1050, 700);
        setLocationRelativeTo(null);
    }

    private void construirInterface() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setContentPane(root);

        JLabel title = new JLabel("Sistema de Irrigação", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 24f));
        title.setBorder(BorderFactory.createEmptyBorder(4, 4, 14, 4));
        root.add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Cadastro de plantas", criarPlantPanel());
        tabs.addTab("Simulação de irrigação", criarSimulationPanel());
        root.add(tabs, BorderLayout.CENTER);

        JLabel footer = new JLabel("Persistência: " + plantDAO.getArquivo().toAbsolutePath());
        footer.setBorder(BorderFactory.createEmptyBorder(8, 4, 0, 4));
        root.add(footer, BorderLayout.SOUTH);
    }

    private JPanel criarPlantPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 4, 4, 4));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados da planta"));
        GridBagConstraints gbc = baseConstraints();

        addField(form, gbc, 0, "Nome:", nomeField);
        addField(form, gbc, 1, "Tipo:", tipoField);
        addField(form, gbc, 2, "Necessidade hídrica (L):", necessidadeField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton saveButton = new JButton("Cadastrar");
        JButton updateButton = new JButton("Atualizar");
        JButton deleteButton = new JButton("Excluir");
        JButton clearButton = new JButton("Limpar");
        buttons.add(saveButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(clearButton);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(buttons, gbc);

        plantTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        plantTable.setAutoCreateRowSorter(true);
        plantTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                carregarLinhaSelecionada();
            }
        });

        JScrollPane scrollPane = new JScrollPane(plantTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Plantas persistidas"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, scrollPane);
        split.setResizeWeight(0.35);
        split.setDividerLocation(350);
        panel.add(split, BorderLayout.CENTER);

        saveButton.addActionListener(event -> cadastrarPlanta());
        updateButton.addActionListener(event -> atualizarPlanta());
        deleteButton.addActionListener(event -> excluirPlanta());
        clearButton.addActionListener(event -> limparFormulario());
        return panel;
    }

    private JPanel criarSimulationPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 4, 4, 4));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados da simulação"));
        GridBagConstraints gbc = baseConstraints();

        addField(form, gbc, 0, "Planta:", plantCombo);
        addField(form, gbc, 1, "Temperatura (°C):", temperaturaField);
        addField(form, gbc, 2, "Umidade do ar (%):", umidadeArField);
        addField(form, gbc, 3, "Precipitação (mm):", precipitacaoField);
        addField(form, gbc, 4, "Profundidade do solo (cm):", profundidadeField);
        addField(form, gbc, 5, "Umidade atual do solo (%):", umidadeSoloField);
        addField(form, gbc, 6, "Capacidade de retenção (%):", retencaoField);
        addField(form, gbc, 7, "Horário (HH:mm):", horarioField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton calculateButton = new JButton("Calcular recomendação");
        JButton startButton = new JButton("Iniciar irrigação");
        JButton stopButton = new JButton("Parar irrigação");
        buttons.add(calculateButton);
        buttons.add(startButton);
        buttons.add(stopButton);

        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        form.add(buttons, gbc);

        resultadoArea.setEditable(false);
        resultadoArea.setLineWrap(true);
        resultadoArea.setWrapStyleWord(true);
        resultadoArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        resultadoArea.setText("Selecione uma planta e informe os dados para calcular a recomendação.");
        JScrollPane resultScroll = new JScrollPane(resultadoArea);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Resultado"));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, form, resultScroll);
        split.setResizeWeight(0.48);
        split.setDividerLocation(470);
        panel.add(split, BorderLayout.CENTER);

        calculateButton.addActionListener(event -> calcularRecomendacao());
        startButton.addActionListener(event -> iniciarIrrigacao());
        stopButton.addActionListener(event -> pararIrrigacao());
        return panel;
    }

    private GridBagConstraints baseConstraints() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        return gbc;
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label,
            java.awt.Component component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(component, gbc);
    }

    private void cadastrarPlanta() {
        try {
            Plant plant = criarPlantaDoFormulario(0);
            plantDAO.salvar(plant);
            carregarPlantas();
            limparFormulario();
            showInfo("Planta cadastrada e salva no arquivo com sucesso.");
        } catch (IllegalArgumentException | IOException exception) {
            showError(exception);
        }
    }

    private void atualizarPlanta() {
        if (selectedPlantId == null) {
            showWarning("Selecione uma planta na tabela para atualizar.");
            return;
        }
        try {
            Plant plant = criarPlantaDoFormulario(selectedPlantId);
            plantDAO.atualizar(plant);
            carregarPlantas();
            limparFormulario();
            showInfo("Planta atualizada com sucesso.");
        } catch (IllegalArgumentException | IOException exception) {
            showError(exception);
        }
    }

    private void excluirPlanta() {
        if (selectedPlantId == null) {
            showWarning("Selecione uma planta na tabela para excluir.");
            return;
        }
        int answer = JOptionPane.showConfirmDialog(this,
                "Deseja realmente excluir a planta selecionada?", "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            if (!plantDAO.excluir(selectedPlantId)) {
                showWarning("A planta selecionada não foi encontrada.");
            } else {
                carregarPlantas();
                limparFormulario();
                showInfo("Planta excluída com sucesso.");
            }
        } catch (IllegalArgumentException | IOException exception) {
            showError(exception);
        }
    }

    private Plant criarPlantaDoFormulario(int id) {
        double necessidade = parseDouble(necessidadeField.getText(), "necessidade hídrica");
        return new Plant(id, nomeField.getText(), tipoField.getText(), necessidade);
    }

    private void carregarLinhaSelecionada() {
        int viewRow = plantTable.getSelectedRow();
        if (viewRow < 0) {
            return;
        }
        int row = plantTable.convertRowIndexToModel(viewRow);
        selectedPlantId = ((Number) tableModel.getValueAt(row, 0)).intValue();
        nomeField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        tipoField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        necessidadeField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
    }

    private void carregarPlantas() {
        try {
            List<Plant> plantas = plantDAO.listar();
            tableModel.setRowCount(0);
            plantCombo.removeAllItems();
            for (Plant plant : plantas) {
                tableModel.addRow(new Object[] { plant.getId(), plant.getNome(), plant.getTipo(),
                        plant.getNecessidadeHidrica() });
                plantCombo.addItem(plant);
            }
        } catch (IOException exception) {
            showError(exception);
        }
    }

    private void limparFormulario() {
        selectedPlantId = null;
        nomeField.setText("");
        tipoField.setText("");
        necessidadeField.setText("");
        plantTable.clearSelection();
        nomeField.requestFocusInWindow();
    }

    private void calcularRecomendacao() {
        try {
            IrrigationRecommendation recommendation = criarRecomendacao();
            currentIrrigation = new IrrigationSystem(recommendation.getVolumeRecomendado(),
                    horarioField.getText().trim());
            resultadoArea.setText(formatarResultado(recommendation));
        } catch (IllegalArgumentException | UmidadeInvalidaException exception) {
            showError(exception);
        }
    }

    private IrrigationRecommendation criarRecomendacao()
            throws UmidadeInvalidaException {
        Plant plant = (Plant) plantCombo.getSelectedItem();
        if (plant == null) {
            throw new IllegalArgumentException(
                    "Cadastre e selecione uma planta antes de realizar a simulação.");
        }

        Weather weather = new Weather(
                parseDouble(temperaturaField.getText(), "temperatura"),
                parseDouble(umidadeArField.getText(), "umidade do ar"),
                parseDouble(precipitacaoField.getText(), "precipitação"));

        SoilLayer layer = new SoilLayer(
                parseDouble(profundidadeField.getText(), "profundidade"),
                parseDouble(umidadeSoloField.getText(), "umidade do solo"),
                parseDouble(retencaoField.getText(), "capacidade de retenção"));
        SoilProfile profile = new SoilProfile();
        profile.adicionarCamada(layer);

        return irrigationService.calcularRecomendacao(plant, weather, profile);
    }

    private String formatarResultado(IrrigationRecommendation recommendation) {
        return String.format(
                "Evapotranspiração estimada: %.2f mm/dia%n"
                        + "Umidade média do solo:       %.2f%%%n"
                        + "Déficit médio do solo:       %.2f%%%n"
                        + "Volume recomendado:          %.2f L%n%n%s",
                recommendation.getEvapotranspiracao(),
                recommendation.getUmidadeMediaSolo(),
                recommendation.getDeficitMedioSolo(),
                recommendation.getVolumeRecomendado(),
                recommendation.getJustificativa());
    }

    private void iniciarIrrigacao() {
        if (currentIrrigation == null) {
            calcularRecomendacao();
        }
        if (currentIrrigation != null) {
            resultadoArea.append("\n\n" + currentIrrigation.iniciarIrrigacao());
        }
    }

    private void pararIrrigacao() {
        if (currentIrrigation == null) {
            showWarning("Nenhuma irrigação foi calculada ou iniciada.");
            return;
        }
        resultadoArea.append("\n" + currentIrrigation.pararIrrigacao());
    }

    private double parseDouble(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("O campo " + fieldName + " é obrigatório.");
        }
        try {
            return Double.parseDouble(value.trim().replace(',', '.'));
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "Informe um número válido no campo " + fieldName + ".");
        }
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(this, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message, "Atenção", JOptionPane.WARNING_MESSAGE);
    }

    private void showError(Exception exception) {
        JOptionPane.showMessageDialog(this, exception.getMessage(), "Erro",
                JOptionPane.ERROR_MESSAGE);
    }
}
