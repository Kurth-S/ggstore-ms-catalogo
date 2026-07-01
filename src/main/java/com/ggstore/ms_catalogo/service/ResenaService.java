package com.ggstore.ms_catalogo.service;

import com.ggstore.ms_catalogo.dto.ResenaDTO;
import com.ggstore.ms_catalogo.model.Resena;
import com.ggstore.ms_catalogo.repository.JuegoRepository;
import com.ggstore.ms_catalogo.repository.ResenaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final JuegoRepository juegoRepository;

    @Transactional(readOnly = true)
    public List<ResenaDTO> listarPorJuego(UUID juegoId) {
        return resenaRepository.findByJuegoId(juegoId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResenaDTO crear(ResenaDTO dto) {
        Resena resena = new Resena();
        resena.setUsuarioId(dto.getUsuarioId());
        resena.setPuntuacion(dto.getPuntuacion());
        resena.setComentario(dto.getComentario());
        juegoRepository.findById(dto.getJuegoId()).ifPresent(resena::setJuego);
        return toDTO(resenaRepository.save(resena));
    }

    @Transactional
    public void eliminar(UUID id) {
        resenaRepository.deleteById(id);
    }

    private ResenaDTO toDTO(Resena resena) {
        ResenaDTO dto = new ResenaDTO();
        dto.setId(resena.getId());
        dto.setJuegoId(resena.getJuego().getId());
        dto.setUsuarioId(resena.getUsuarioId());
        dto.setPuntuacion(resena.getPuntuacion());
        dto.setComentario(resena.getComentario());
        dto.setCreatedAt(resena.getCreatedAt());
        return dto;
    }
}
