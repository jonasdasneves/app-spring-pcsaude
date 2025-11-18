package br.com.pcsaude.controller;

import br.com.pcsaude.entities.Suporte;
import br.com.pcsaude.entities.Usuario;
import br.com.pcsaude.mappers.MedicaoMapper;
import br.com.pcsaude.mappers.SuporteMapper;
import br.com.pcsaude.records.SuporteInDto;
import br.com.pcsaude.records.SuporteOutDto;
import br.com.pcsaude.services.SuporteService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/suportes")
public class SuporteController {

    private final SuporteService service;

    public SuporteController(SuporteService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public SuporteOutDto findById(@PathVariable Long id) {
        Suporte suporte = this.service.findById(id);
        return SuporteMapper.toDto(suporte);
    }

    @GetMapping
    public Page<SuporteOutDto> findAll(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {
        return this.service
                .findAll(page, size)
                .map(SuporteMapper::toDto);
    }

    @PostMapping
    public SuporteOutDto save(@RequestBody SuporteInDto dto) {

        //TO DO Usuário deve ser definido por contexto
        Usuario usuario = new Usuario();

        Suporte suporte = SuporteMapper.fromDto(usuario, dto);
        Suporte newSuporte = this.service.save(suporte);
        return SuporteMapper.toDto(newSuporte);
    }

    @PutMapping("/{id}")
    public SuporteOutDto cancelar(@PathVariable Long id) {
        Suporte suporteAtualizado = this.service.cancelar(id);
        return SuporteMapper.toDto(suporteAtualizado);
    }
}
