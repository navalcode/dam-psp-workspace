package com.salesianostriana.dam._PrimerProyectoSpringData;

import static org.assertj.core.api.Assertions.assertThat;

import com.salesianostriana.dam._PrimerProyectoSpringData.model.Alumno;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class StudentPersistenceTests {
  @Autowired TestEntityManager em;

  private Long create() {
    Alumno student = new Alumno();
    student.setNombre("Ada");
    student.setApellido("Demo");
    student.setEmail("ada@example.invalid");
    em.persistAndFlush(student);
    Long id = student.getId();
    em.clear();
    return id;
  }

  @Test
  void assignedIdentityAndFieldsSurviveReload() {
    Long id = create();
    assertThat(id).isNotNull();
    Alumno reloaded = em.find(Alumno.class, id);
    assertThat(reloaded.getNombre()).isEqualTo("Ada");
    assertThat(reloaded.getApellido()).isEqualTo("Demo");
    assertThat(reloaded.getEmail()).isEqualTo("ada@example.invalid");
  }

  @Test
  void managedUpdateSurvivesFlushAndReload() {
    Long id = create();
    em.find(Alumno.class, id).setApellido("Example");
    em.flush();
    em.clear();
    assertThat(em.find(Alumno.class, id).getApellido()).isEqualTo("Example");
  }

  @Test
  void removalIsVisibleAfterFlushAndClear() {
    Long id = create();
    em.remove(em.find(Alumno.class, id));
    em.flush();
    em.clear();
    assertThat(em.find(Alumno.class, id)).isNull();
  }
}
