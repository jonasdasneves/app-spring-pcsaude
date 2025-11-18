package br.com.pcsaude.mappers;

import br.com.pcsaude.entities.Suporte;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.records.SuporteInDto;
import br.com.pcsaude.records.SuporteOutDto;

public class SuporteMapper {

    public static Suporte fromDto(Usuario usuario, SuporteInDto dto){
        return new Suporte(
                usuario,
                dto.titulo(),
                dto.descricao());
    }

    public static SuporteOutDto toDto(Suporte suporte){
        return new SuporteOutDto(
                suporte.getId(),
                suporte.getTitulo(),
                suporte.getDescricao(),
                suporte.getDataReclamacao(),
                suporte.getDataResolucao(),
                suporte.getStatus());
    }

    private SuporteMapper() {}
}
