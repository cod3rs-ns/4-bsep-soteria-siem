package bsep.sw.hateoas.user;


import bsep.sw.domain.Project;
import bsep.sw.domain.User;
import bsep.sw.hateoas.relationships.RelationshipData;
import bsep.sw.hateoas.relationships.RelationshipLinks;
import bsep.sw.hateoas.relationships.ResponseCollectionRelationship;

import java.util.ArrayList;
import java.util.List;

import static bsep.sw.hateoas.ResourceTypes.PROJECTS_TYPE;

public class UserResponseRelationships {

    private ResponseCollectionRelationship projects;
    private ResponseCollectionRelationship ownedProjects;

    public static UserResponseRelationships fromDomain(final User user) {
        final List<RelationshipData> projectsData = new ArrayList<>();
        for (Project project : user.getProjects()) {
            projectsData.add(new RelationshipData(PROJECTS_TYPE, project.getId().toString()));
        }
        // FIXME
        final RelationshipLinks projectLinks = new RelationshipLinks( "related-link");
        final ResponseCollectionRelationship projects = new ResponseCollectionRelationship(projectLinks, projectsData);

        final List<RelationshipData> ownedProjectsData = new ArrayList<>();
        for (Project project : user.getOwnedProjects()) {
            ownedProjectsData.add(new RelationshipData(PROJECTS_TYPE, project.getId().toString()));
        }
        // FIXME
        final RelationshipLinks ownedProjectLinks = new RelationshipLinks( "related-link");
        final ResponseCollectionRelationship ownedProjects = new ResponseCollectionRelationship(ownedProjectLinks, ownedProjectsData);

        return new UserResponseRelationships(projects, ownedProjects);
    }

    public UserResponseRelationships(ResponseCollectionRelationship projects, ResponseCollectionRelationship ownedProjects) {
        this.projects = projects;
        this.ownedProjects = ownedProjects;
    }

    public ResponseCollectionRelationship getProjects() {
        return projects;
    }

    public void setProjects(ResponseCollectionRelationship projects) {
        this.projects = projects;
    }

    public ResponseCollectionRelationship getOwnedProjects() {
        return ownedProjects;
    }

    public void setOwnedProjects(ResponseCollectionRelationship ownedProjects) {
        this.ownedProjects = ownedProjects;
    }
}
