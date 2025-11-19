package br.com.pcsaude.mappers;

import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.enums.ModeloTrabalhoEnum;
import br.com.pcsaude.enums.Role;
import br.com.pcsaude.enums.SexoEnum;
import br.com.pcsaude.records.UsuarioInDto;
import br.com.pcsaude.records.UsuarioOutDto;

import java.math.BigDecimal;

public class UsuarioMapper {
    public static Usuario fromDto(Dispositivo dispositivo, UsuarioInDto dto){
        return new Usuario(
                null, //Role será USER
                dto.nome(),
                dto.email(),
                dto.senha(),
                dto.altura(),
                dto.peso(),
                dto.sexo(),
                dto.modeloTrabalho(),
                dispositivo
        );
    }

    public static UsuarioOutDto toDto(Usuario usuario){

        String uuid = null;

        if (usuario.getDispositivo() != null){
            uuid = usuario.getDispositivo().getUuid();
        }

        return new UsuarioOutDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getAltura(),
                usuario.getPeso(),
                usuario.getSexo(),
                usuario.getModeloTrabalho(),
                usuario.getRole(),
                usuario.getDataCadastro(),
                uuid
        );
    }
}
