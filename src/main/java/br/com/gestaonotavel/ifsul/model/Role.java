package br.com.gestaonotavel.ifsul.model;

import java.util.HashSet;
import java.util.Set;
import java.util.Arrays;

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

    // O Admin pode fazer, literalmente, tudo
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

    // Conjunto de permissões para cada um dos papéis
    private final Set<Permission> permissions;

    /**
     * Construtor do Enum que aceita uma lista de permissões.
     * @param permissions permissões associadas ao papel.
     */
    Role(Permission... permissions) {
        this.permissions = new HashSet<>(Arrays.asList(permissions));
    }

    /**
     * Retorna o conjunto de permissões do papel.
     * @return um Set<Permission>
     */
    public Set<Permission> getPermissions() {
        return permissions;
    }

    /**
     * Verifica se este papel contém uma permissão específica.
     * @param permission a permissão a ser verificada.
     * @return true se o papel tiver a permissão, false caso contrário.
     */
    public boolean hasPermission(Permission permission) {
        if (permission == null) {
            return false;
        }
        return this.permissions.contains(permission);
    }
}
