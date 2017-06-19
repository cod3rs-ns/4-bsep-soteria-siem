package bsep.sw.security;

import bsep.sw.domain.Privilege;
import bsep.sw.domain.Role;
import bsep.sw.repositories.PrivilegeRepository;
import bsep.sw.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Component
public class InitialRolesLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private boolean alreadySetup = false;

    @Autowired
    public InitialRolesLoader(final RoleRepository roleRepository, final PrivilegeRepository privilegeRepository) {
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) return;

        // all privileges
        final Privilege writeAgent = createPrivilegeIfNotFound(Privileges.WRITE_AGENT);
        final Privilege readAgent = createPrivilegeIfNotFound(Privileges.READ_AGENT);
        final Privilege downloadAgent = createPrivilegeIfNotFound(Privileges.DOWNLOAD_AGENT);
        final Privilege readAlarm = createPrivilegeIfNotFound(Privileges.READ_ALARM);
        final Privilege writeAlarm = createPrivilegeIfNotFound(Privileges.WRITE_ALARM);
        final Privilege readDefinition = createPrivilegeIfNotFound(Privileges.READ_ALARM_DEFINITION);
        final Privilege writeDefinition = createPrivilegeIfNotFound(Privileges.WRITE_ALARM_DEFINITION);
        final Privilege removeDefinition = createPrivilegeIfNotFound(Privileges.REMOVE_ALARM_DEFINITION);
        final Privilege readProject = createPrivilegeIfNotFound(Privileges.READ_PROJECT);
        final Privilege writeProject = createPrivilegeIfNotFound(Privileges.WRITE_PROJECT);
        final Privilege removeProject = createPrivilegeIfNotFound(Privileges.REMOVE_PROJECT);
        final Privilege readCollaborators = createPrivilegeIfNotFound(Privileges.READ_PROJECT_COLLABORATORS);
        final Privilege writeCollaborators = createPrivilegeIfNotFound(Privileges.WRITE_PROJECT_COLLABORATORS);
        final Privilege readReport = createPrivilegeIfNotFound(Privileges.READ_REPORT);
        final Privilege readSelfInfo = createPrivilegeIfNotFound(Privileges.READ_SELF_INFO);

        // basic privileges collection for all users
        final Collection<Privilege> basicPrivileges = new ArrayList<>();
        basicPrivileges.add(writeAgent);
        basicPrivileges.add(readAgent);
        basicPrivileges.add(downloadAgent);
        basicPrivileges.add(readAlarm);
        basicPrivileges.add(writeAlarm);
        basicPrivileges.add(readDefinition);
        basicPrivileges.add(readProject);
        basicPrivileges.add(readCollaborators);
        basicPrivileges.add(readReport);
        basicPrivileges.add(readSelfInfo);

        // admin only privileges
        final Collection<Privilege> adminPrivileges = new ArrayList<>();
        adminPrivileges.add(writeDefinition);
        adminPrivileges.add(removeDefinition);
        adminPrivileges.add(writeProject);
        adminPrivileges.add(removeProject);
        adminPrivileges.add(writeCollaborators);

        // create ADMIN role
        final Set<Privilege> allAdminPrivileges = new HashSet<>();
        allAdminPrivileges.addAll(basicPrivileges);
        allAdminPrivileges.addAll(adminPrivileges);
        createRoleIfNotFound("ROLE_ADMIN", allAdminPrivileges);

        // create OPERATOR and FACEBOOK role
        final Set<Privilege> allBasicPrivileges = new HashSet<>();
        allBasicPrivileges.addAll(basicPrivileges);
        createRoleIfNotFound("ROLE_OPERATOR", allBasicPrivileges);
        createRoleIfNotFound("ROLE_FACEBOOK", allBasicPrivileges);

        alreadySetup = true;
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(final String name) {
        Privilege privilege = privilegeRepository.findPrivilegeByName(name);

        if (privilege == null) {
            privilege = privilegeRepository.save(new Privilege().name(name));
        }

        return privilege;
    }

    @Transactional
    private void createRoleIfNotFound(final String name, final Set<Privilege> privileges) {
        Role role = roleRepository.findRoleByName(name);
        if (role == null) {
            role = new Role().name(name);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
    }

}
