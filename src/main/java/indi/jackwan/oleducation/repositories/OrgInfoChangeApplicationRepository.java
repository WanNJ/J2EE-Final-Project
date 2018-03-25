package indi.jackwan.oleducation.repositories;

import indi.jackwan.oleducation.models.OrgInfoChangeApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrgInfoChangeApplicationRepository extends CrudRepository<OrgInfoChangeApplication, Long> {
    List<OrgInfoChangeApplication> findByDeclinedAndApproved(boolean declined, boolean approved);
    OrgInfoChangeApplication findById(int id);
}
