package verrimar.coopcycle.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import verrimar.coopcycle.web.rest.TestUtil;

class CommercantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Commercant.class);
        Commercant commercant1 = new Commercant();
        commercant1.setId(1L);
        Commercant commercant2 = new Commercant();
        commercant2.setId(commercant1.getId());
        assertThat(commercant1).isEqualTo(commercant2);
        commercant2.setId(2L);
        assertThat(commercant1).isNotEqualTo(commercant2);
        commercant1.setId(null);
        assertThat(commercant1).isNotEqualTo(commercant2);
    }
}
