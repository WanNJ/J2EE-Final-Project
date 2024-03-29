package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    List<Organization> findAll();
    Organization findById(int id);
    Organization findByName(String name);
    Organization findByOrgCode(String orgCode);
    List<Organization> findByDeclinedAndEnabled(boolean declined, boolean approved);
    long count();
    int countOrganizationsByEnabledAndDeclined(boolean enabled, boolean declined);
    int countOrganizationsByCoursesIsNotNull();
}