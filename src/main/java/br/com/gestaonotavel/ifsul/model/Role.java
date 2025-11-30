package br.com.gestaonotavel.ifsul.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static br.com.gestaonotavel.ifsul.model.Permission.*;

public enum Role {
    SECRETARIO(
            VISUALIZAR_PACIENTES,
            CADASTRAR_PACIENTE,
            EDITAR_PACIENTE,
            CRIAR_AGENDAMENTO,
            EDITAR_AGENDAMENTO,
            CANCELAR_AGENDAMENTO,
            REGISTRAR_VOLUNTARIADO,
            VER_VOLUNTARIADO,
            VER_FINANCEIRO,
            GERAR_RELATORIOS
    ),

    ADMIN(
            VISUALIZAR_PACIENTES,
            CADASTRAR_PACIENTE,
            EDITAR_PACIENTE,
            EXCLUIR_PACIENTE,
            CRIAR_AGENDAMENTO,
            EDITAR_AGENDAMENTO,
            CANCELAR_AGENDAMENTO,
            REGISTRAR_VOLUNTARIADO,
            VER_VOLUNTARIADO,
            VER_FINANCEIRO,
            EDITAR_FINANCEIRO,
            GERAR_RELATORIOS,
            ADMINISTRAR_USUARIOS
    );

    private final Set<Permission> permissions;

    Role(Permission... permissions) {
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission permission) {
        if (permission == null) {
            return false;
        }
        return this.permissions.contains(permission);
    }
}