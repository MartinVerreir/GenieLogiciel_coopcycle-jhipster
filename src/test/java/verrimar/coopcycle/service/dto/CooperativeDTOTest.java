package verrimar.coopcycle.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import verrimar.coopcycle.web.rest.TestUtil;

class CooperativeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CooperativeDTO.class);
        CooperativeDTO cooperativeDTO1 = new CooperativeDTO();
        cooperativeDTO1.setId(1L);
        CooperativeDTO cooperativeDTO2 = new CooperativeDTO();
        assertThat(cooperativeDTO1).isNotEqualTo(cooperativeDTO2);
        cooperativeDTO2.setId(cooperativeDTO1.getId());
        assertThat(cooperativeDTO1).isEqualTo(cooperativeDTO2);
        cooperativeDTO2.setId(2L);
        assertThat(cooperativeDTO1).isNotEqualTo(cooperativeDTO2);
        cooperativeDTO1.setId(null);
        assertThat(cooperativeDTO1).isNotEqualTo(cooperativeDTO2);
    }
}
