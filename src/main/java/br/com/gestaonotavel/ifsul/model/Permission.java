package br.com.gestaonotavel.ifsul.model;

/**
 * Enum que define todas as permissões granulares do sistema.
 */
public enum Permission {
    // Permissões de Paciente
    VISUALIZAR_PACIENTES,
    CADASTRAR_PACIENTE,
    EDITAR_PACIENTE,
    EXCLUIR_PACIENTE,

    // Permissões de Agendamento
    CRIAR_AGENDAMENTO,
    EDITAR_AGENDAMENTO,
    CANCELAR_AGENDAMENTO,

    // Permissões de Voluntariado
    REGISTRAR_VOLUNTARIADO,
    VER_VOLUNTARIADO,

    // Permissões Financeiras
    VER_FINANCEIRO,
    EDITAR_FINANCEIRO, // Permissão crítica (só admin)
    GERAR_RELATORIOS,

    // Permissões de Sistema
    ADMINISTRAR_USUARIOS
}