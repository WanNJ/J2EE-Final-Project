package indi.jackwan.oleducation.service;


import indi.jackwan.oleducation.models.*;
import indi.jackwan.oleducation.models.Class;
import indi.jackwan.oleducation.repositories.ClassRepository;
import indi.jackwan.oleducation.repositories.ClassSignInRepository;
import indi.jackwan.oleducation.repositories.GradeRepository;
import indi.jackwan.oleducation.utils.Enums.SignInStudentResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("orgClassService")
public class ClassService {
    @Autowired
    private UserService userService;
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    private ClassSignInRepository classSignInRepository;
    @Autowired
    private GradeRepository gradeRepository;

    public SignInStudentResult signInStudent(int classId, String email, Organization organization) {
        User user = userService.findByEmail(email);
        Class aClass = classRepository.findByIdAndOrganization(classId, organization);

        if (user == null)
            return SignInStudentResult.NO_SUCH_USER;
        else if (aClass == null)
            return SignInStudentResult.NO_SUCH_CLASS_OR_NO_ACCESS_TO_CLASS;
        else {
            if (user.getClassList().contains(aClass)) {
                ClassSignIn signInForm = new ClassSignIn();
                signInForm.setaClass(aClass);
                signInForm.setUser(user);
                classSignInRepository.save(signInForm);
                return SignInStudentResult.SUCCESS;
            } else {
                return SignInStudentResult.USER_NOT_IN_CLASS;
            }
        }
    }

    public SignInStudentResult recordGrades(double score, int classId, String email, Organization organization) {
        User user = userService.findByEmail(email);
        Class aClass = classRepository.findByIdAndOrganization(classId, organization);

        if (user == null)
            return SignInStudentResult.NO_SUCH_USER;
        else if (aClass == null)
            return SignInStudentResult.NO_SUCH_CLASS_OR_NO_ACCESS_TO_CLASS;
        else {
            if (user.getClassList().contains(aClass)) {
                Grade grade = new Grade();
                grade.setaClass(aClass);
                grade.setUser(user);
                grade.setScore(score);
                gradeRepository.save(grade);
                return SignInStudentResult.SUCCESS;
            } else {
                return SignInStudentResult.USER_NOT_IN_CLASS;
            }
        }
    }
}
