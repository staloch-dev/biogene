package com.biogene.enums;

import java.util.List;

public enum ExamType {

    // ALERGIA E INTOLERÂNCIA
    PAINEL_ALERGIAS_IGE(ExamCategory.ALERGIA_INTOLERANCIA,
            List.of(SampleType.SORO)),

    PAINEL_SENSIBILIDADE_ALIMENTAR_IGG(ExamCategory.ALERGIA_INTOLERANCIA,
            List.of(SampleType.SORO)),

    TESTE_INTOLERANCIA_LACTOSE(ExamCategory.ALERGIA_INTOLERANCIA,
            List.of(SampleType.SANGUE, SampleType.SALIVA)),

    // ANDROLOGIA
    ESPERMOGRAMA(ExamCategory.ANDROLOGIA,
            List.of(SampleType.SEMEN)),

    FRAGMENTACAO_DNA_ESPERMATICO(ExamCategory.ANDROLOGIA,
            List.of(SampleType.SEMEN)),

    // BIOLOGIA MOLECULAR
    HPV(ExamCategory.BIOLOGIA_MOLECULAR,
            List.of(SampleType.SWAB_CERVICAL, SampleType.SWAB_VAGINAL,
                    SampleType.SWAB_RETAL, SampleType.SEMEN)),

    PCR_SARS_COV_2(ExamCategory.BIOLOGIA_MOLECULAR,
            List.of(SampleType.SWAB_NASOFARINGEO, SampleType.SWAB_NASAL,
                    SampleType.SWAB_OROFARINGEO, SampleType.SALIVA)),

    // BIOQUÍMICA
    ACIDO_FOLICO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.PLASMA)),

    ACIDO_URICO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    ALBUMINA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    AMILASE(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    BILIRRUBINAS(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    CALCIO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    COLESTEROL_TOTAL(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    CREATININA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    DOSAGEM_DE_POTASSIO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    DOSAGEM_DE_SODIO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    ELETROFORESE_DE_PROTEINAS(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    FERRITINA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    FERRO_SERICO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    FOSFATASE_ALCALINA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    FOSFORO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    GAMA_GT(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    GASOMETRIA_ARTERIAL(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SANGUE_ARTERIAL)),

    GLICEMIA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.PLASMA, SampleType.SORO)),

    HEMOGLOBINA_GLICADA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SANGUE)),

    LIPASE(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    MAGNESIO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    MICROALBUMINURIA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.URINA)),

    PROTEINA_C_REATIVA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    PROTEINAS_TOTAIS(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    TGO(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    TGP(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    TRIGLICERIDEOS(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    UREIA(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO, SampleType.URINA)),

    VITAMINA_B12(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    VITAMINA_D(ExamCategory.BIOQUIMICA,
            List.of(SampleType.SORO)),

    // CITOLOGIA
    CITOLOGIA_ONCOTICA_LIQUIDOS(ExamCategory.CITOLOGIA,
            List.of(SampleType.LIQUIDO_PLEURAL, SampleType.LIQUIDO_ASCITICO,
                    SampleType.LIQUOR, SampleType.URINA)),

    PAPANICOLAU(ExamCategory.CITOLOGIA,
            List.of(SampleType.SWAB_CERVICAL, SampleType.SWAB_VAGINAL)),

    // GENÉTICA
    CARIOTIPO(ExamCategory.GENETICA,
            List.of(SampleType.SANGUE, SampleType.MEDULA_OSSEA, SampleType.TECIDO)),

    DNA_FETAL(ExamCategory.GENETICA,
            List.of(SampleType.SANGUE)),

    PAINEL_GENETICO(ExamCategory.GENETICA,
            List.of(SampleType.SANGUE, SampleType.SALIVA, SampleType.SWAB_BUCAL)),

    TESTE_DE_PATERNIDADE(ExamCategory.GENETICA,
            List.of(SampleType.SANGUE, SampleType.SALIVA,
                    SampleType.SWAB_BUCAL, SampleType.TECIDO)),

    // HEMATOLOGIA
    COAGULOGRAMA(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.PLASMA)),

    COOMBS_DIRETO(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.SANGUE)),

    COOMBS_INDIRETO(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.SANGUE, SampleType.PLASMA)),

    HEMOGRAMA(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.SANGUE)),

    MIELOGRAMA(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.MEDULA_OSSEA)),

    RETICULOCITOS(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.SANGUE)),

    TIPAGEM_SANGUINEA_FATOR_RH(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.SANGUE)),

    VELOCIDADE_DE_HEMOSSEDIMENTACAO(ExamCategory.HEMATOLOGIA,
            List.of(SampleType.SANGUE)),

    // HORMONAL
    ANTICORPOS_ANTI_TPO(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    BETA_HCG(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    CORTISOL(ExamCategory.HORMONAL,
            List.of(SampleType.SORO, SampleType.SALIVA, SampleType.URINA)),

    ESTRADIOL(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    FSH(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    INSULINA(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    LH(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    PEPTIDEO_C(ExamCategory.HORMONAL,
            List.of(SampleType.SORO, SampleType.URINA)),

    PROGESTERONA(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    PROLACTINA(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    PSA(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    SHBG(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    TESTOSTERONA(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    TSH(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    T4_LIVRE(ExamCategory.HORMONAL,
            List.of(SampleType.SORO)),

    // IMUNOLOGIA
    ANTICORPOS_ANTINUCLEARES(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    ANTICORPOS_ANTI_DNA(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    ANTICORPOS_ANTI_HBS(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    ANTICORPOS_ANTI_HCV(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    ANTICORPOS_ANTI_HIV(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    BETA2_MICROGLOBULINA(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO, SampleType.URINA)),

    CHIKUNGUNYA(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    CITOMEGALOVIRUS(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO, SampleType.SANGUE)),

    COMPLEMENTO_C3(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    COMPLEMENTO_C4(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    DENGUE(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    FATOR_REUMATOIDE(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    HEPATITE_B(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    HEPATITE_C(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    HERPES(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO, SampleType.SWAB_NASOFARINGEO,
                    SampleType.SECRECAO_OCULAR)),

    HIV(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    RUBEOLA(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    SIFILIS(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    SOROLOGIA_CELIACA(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    SOROLOGIAS_HEPATITES(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    TOXOPLASMOSE(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO)),

    ZIKA(ExamCategory.IMUNOLOGIA,
            List.of(SampleType.SORO, SampleType.URINA)),

    // MICOLOGIA
    ANTIFUNGIGRAMA(ExamCategory.MICOLOGIA,
            List.of(SampleType.SECRECAO_VAGINAL, SampleType.SECRECAO_URETRAL,
                    SampleType.LIQUIDO_PLEURAL, SampleType.LIQUIDO_ASCITICO,
                    SampleType.SANGUE, SampleType.URINA, SampleType.LIQUOR)),

    CULTURA_PARA_FUNGOS(ExamCategory.MICOLOGIA,
            List.of(SampleType.RASPADO_CUTANEO, SampleType.SWAB_VAGINAL,
                    SampleType.SECRECAO_VAGINAL, SampleType.ESCARRO,
                    SampleType.LAVADO_BRONCOALVEOLAR, SampleType.URINA,
                    SampleType.SANGUE, SampleType.LIQUOR,
                    SampleType.LIQUIDO_PLEURAL, SampleType.LIQUIDO_ASCITICO)),

    MICOLOGICO_DIRETO(ExamCategory.MICOLOGIA,
            List.of(SampleType.RASPADO_CUTANEO, SampleType.SWAB_VAGINAL,
                    SampleType.SECRECAO_VAGINAL, SampleType.SWAB_NASOFARINGEO,
                    SampleType.ESCARRO, SampleType.URINA,
                    SampleType.SECRECAO_OCULAR)),

    // MICROBIOLOGIA
    ANTIBIOGRAMA(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.SECRECAO_URETRAL, SampleType.SECRECAO_VAGINAL,
                    SampleType.SECRECAO_OCULAR, SampleType.SECRECAO_NASAL,
                    SampleType.SWAB_OROFARINGEO, SampleType.ESCARRO,
                    SampleType.URINA, SampleType.SANGUE,
                    SampleType.LIQUIDO_PLEURAL, SampleType.LIQUIDO_ASCITICO)),

    COPROCULTURA(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.FEZES)),

    CULTURA_BAAR(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.ESCARRO, SampleType.LAVADO_BRONCOALVEOLAR,
                    SampleType.URINA, SampleType.LIQUOR)),

    CULTURA_DE_SECRECOES(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.SECRECAO_VAGINAL, SampleType.SECRECAO_URETRAL,
                    SampleType.SECRECAO_OCULAR, SampleType.SECRECAO_NASAL,
                    SampleType.SWAB_OROFARINGEO, SampleType.SWAB_NASAL,
                    SampleType.ESCARRO, SampleType.LIQUIDO_PLEURAL)),

    HEMOCULTURA(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.SANGUE, SampleType.SANGUE_ARTERIAL)),

    LIQUOR(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.LIQUOR)),

    PESQUISA_BAAR(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.ESCARRO, SampleType.LAVADO_BRONCOALVEOLAR,
                    SampleType.URINA, SampleType.LIQUOR)),

    UROCULTURA(ExamCategory.MICROBIOLOGIA,
            List.of(SampleType.URINA)),

    // ONCOLOGIA
    AFP(ExamCategory.ONCOLOGIA,
            List.of(SampleType.SORO)),

    CA_125(ExamCategory.ONCOLOGIA,
            List.of(SampleType.SORO)),

    CA_15_3(ExamCategory.ONCOLOGIA,
            List.of(SampleType.SORO)),

    CA_19_9(ExamCategory.ONCOLOGIA,
            List.of(SampleType.SORO)),

    CEA(ExamCategory.ONCOLOGIA,
            List.of(SampleType.SORO)),

    // PARASITOLOGIA
    PARASITOLOGICO_DE_FEZES(ExamCategory.PARASITOLOGIA,
            List.of(SampleType.FEZES)),

    SANGUE_OCULTO_FEZES(ExamCategory.PARASITOLOGIA,
            List.of(SampleType.FEZES)),

    // URINALISE
    URINA_TIPO_1(ExamCategory.URINALISE,
            List.of(SampleType.URINA));

    private final ExamCategory category;
    private final List<SampleType> compatibleSamples;

    ExamType(ExamCategory category, List<SampleType> compatibleSamples) {
        this.category = category;
        this.compatibleSamples = compatibleSamples;
    }

    public ExamCategory getCategory() {
        return category;
    }

    public List<SampleType> getCompatibleSamples() {
        return compatibleSamples;
    }

    public SampleType getDefaultSample() {
        return compatibleSamples.get(0);
    }

}
