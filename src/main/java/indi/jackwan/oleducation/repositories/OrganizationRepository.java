package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.Organization;
import org.springframework.data.repository.CrudRepository;

public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    @Override
    Iterable<Organization> findAll();
    Organization findById(int id);
    Organization findByName(String confirmationToken);
    Organization findByOrgCode(String orgCode);
}