package br.com.pcsaude.mappers;

import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Medicao;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.records.MedicaoInDto;
import br.com.pcsaude.records.MedicaoOutDto;

public class MedicaoMapper {

    public static Medicao fromDto(Dispositivo dispositivo, MedicaoInDto dto){
        return new Medicao(
            dispositivo,
            dto.postura(),
            dto.tempoSentadoMin(),
            dto.iluminacaoLux(),
            dto.temperaturaC(),
            dto.alturaTelaCm()
        );
    }

    public static MedicaoOutDto toDto(Medicao medicao){
        return new MedicaoOutDto(
                medicao.getId(),
                medicao.getDispositivo().getUuid(),
                medicao.getPostura(),
                medicao.getTempoSentadoMin(),
                medicao.getIluminacaoLux(),
                medicao.getTemperaturaC(),
                medicao.getAlturaTelaCm(),
                medicao.getMomentoMedicao()
        );
    }

}
