package br.com.pcsaude.controller;

import br.com.pcsaude.entities.Dispositivo;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.mappers.UsuarioMapper;
import br.com.pcsaude.records.AuthResponse;
import br.com.pcsaude.records.LoginRequest;
import br.com.pcsaude.records.UsuarioInDto;
import br.com.pcsaude.records.UsuarioOutDto;
import br.com.pcsaude.security.JwtUtil;
import br.com.pcsaude.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Login", description = "Autentica o usuário e retorna um token JWT.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação bem sucedida"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou dados de login incorretos")
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Parameter(description = "Dados de login (email e senha)", required = true) @Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = jwtUtil.generateToken(authentication);

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        AuthResponse response = new AuthResponse(token, request.email(), roles);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Cadastrar usuário", description = "Cria um novo usuário associado a um dispositivo (por UUID). Retorna o usuário criado.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro")
    })
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioOutDto> cadastrar(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Dados do usuário a ser cadastrado", required = true) @Valid @RequestBody UsuarioInDto dto) {

        Dispositivo dispositivo = new Dispositivo(dto.dispositivo_uuid());

        Usuario usuario = UsuarioMapper.fromDto(dispositivo, dto);

        Usuario novoUsuario = this.usuarioService.save(usuario);

        return ResponseEntity.ok(UsuarioMapper.toDto(novoUsuario));
    }
}
