package br.com.pcsaude.controller;

import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.mappers.UsuarioMapper;
import br.com.pcsaude.records.UsuarioOutDto;
import br.com.pcsaude.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Buscar usuário por id", description = "Retorna os dados do usuário identificado pelo id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioOutDto> findById(@Parameter(description = "Id do usuário", required = true) @PathVariable Long id) {
        Usuario usuario = service.findById(id);
        UsuarioOutDto usuarioOutDto = UsuarioMapper.toDto(usuario);

        return ResponseEntity.ok(usuarioOutDto);
    }

    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados do usuário e retorna o registro atualizado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para atualização")
    })
    @PutMapping
    public ResponseEntity<UsuarioOutDto> update(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Usuário com os novos dados", required = true) @Valid @RequestBody Usuario usuario) {
        Usuario usuarioUpdate = service.update(usuario);
        UsuarioOutDto usuarioAtualizado =  UsuarioMapper.toDto(usuarioUpdate);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}
