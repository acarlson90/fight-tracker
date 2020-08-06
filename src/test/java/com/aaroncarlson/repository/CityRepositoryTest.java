package com.aaroncarlson.repository;

import com.aaroncarlson.model.City;
import com.aaroncarlson.util.TestConstants;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityRepositoryTest {

    @Autowired
    private CityRepository cityRepository;
    private Optional cityOptional;
    private City sanFrancisco;
    private City walnutCreek;
    private City oakland;
    private City sanDiego;

    @Before
    public void setup() throws Exception {
        sanFrancisco = cityRepository.save(new City(TestConstants.SAN_FRANCISCO));
        walnutCreek = cityRepository.save(new City(TestConstants.WALNUT_CREEK));
        oakland = cityRepository.save(new City(TestConstants.OAKLAND));
        sanDiego = cityRepository.save(new City(TestConstants.SAN_DIEGO));
    }

    @Test
    public void testFindCityByName() throws Exception {
        // Test find City by Name ignore case
        cityOptional = cityRepository.findByNameIgnoreCase(TestConstants.SAN_FRANCISCO.toUpperCase());
        assertThat(((City)cityOptional.get()).getName()).isEqualTo(TestConstants.SAN_FRANCISCO);

        // Test find City by multiple Names - non are correct
        cityOptional = cityRepository.findByNameOrNameIgnoreCase(TestConstants.BERKELEY, TestConstants.BARCELONA);
        assertThat(cityOptional.isPresent()).isFalse();

        // Test find City by multiple Names - first value correct case match
        cityOptional = cityRepository.findByNameOrNameIgnoreCase(TestConstants.SAN_FRANCISCO, TestConstants.BARCELONA);
        assertThat(((City)cityOptional.get()).getName()).isEqualTo(TestConstants.SAN_FRANCISCO);

        // Test find City by multiple Names - last value correct case match
        cityOptional = cityRepository.findByNameOrNameIgnoreCase(TestConstants.BERKELEY, TestConstants.SAN_FRANCISCO);
        assertThat(((City)cityOptional.get()).getName()).isEqualTo(TestConstants.SAN_FRANCISCO);

        // Test find City by multiple Names - last value correct case mismatch
        cityOptional = cityRepository.findByNameOrNameIgnoreCase(TestConstants.BERKELEY, TestConstants.SAN_FRANCISCO.toUpperCase());
        assertThat(((City)cityOptional.get()).getName()).isEqualTo(TestConstants.SAN_FRANCISCO);

        // Test find all Cities EXCEPT a specific one
        assertThat(cityRepository.findByNameNot(TestConstants.SAN_FRANCISCO))
                .hasSize(3)
                .contains(walnutCreek)
                .contains(oakland)
                .contains(sanDiego)
                .doesNotContain(sanFrancisco);

        // Test find Cities starting/ending with a certain Name
        assertThat(cityRepository.findByNameStartingWith("San"))
                .hasSize(2)
                .contains(sanDiego)
                .contains(sanFrancisco);
        assertThat(cityRepository.findByNameEndingWith("land"))
                .hasSize(1)
                .contains(oakland);

        // Test Contain
        assertThat(cityRepository.findByNameContains("Oak"))
                .hasSize(1)
                .contains(oakland);
        assertThat(cityRepository.findByNameIsContaining("Oak"))
                .hasSize(1)
                .contains(oakland);
        assertThat(cityRepository.findByNameContaining("Oak"))
                .hasSize(1)
                .contains(oakland);

        // Test Like
        assertThat(cityRepository.findByNameLike("San%"))
                .hasSize(2)
                .contains(sanFrancisco)
                .contains(sanDiego);
        assertThat(cityRepository.findByNameLike("San Fran%"))
                .hasSize(1)
                .contains(cityRepository.queryByName("San Fran").get(0));
        assertThat(cityRepository.findByNameLike("%land"))
                .hasSize(1)
                .contains(oakland);
        assertThat(cityRepository.findByNameLike("%nut%"))
                .hasSize(1)
                .containsExactly(walnutCreek);
        assertThat(cityRepository.findByNameLike("%NUT%"))
                .hasSize(0)
                .doesNotContain(walnutCreek);
        assertThat(cityRepository.findByNameLikeIgnoreCase("%NUT%"))
                .hasSize(1)
                .containsExactly(walnutCreek);

        // Find All Cities (that have a name)
        assertThat(cityRepository.findByNameIsNotNull())
                .hasSize(4)
                .contains(sanFrancisco)
                .contains(walnutCreek)
                .contains(oakland)
                .contains(sanDiego);

        assertThat(cityRepository.findByNameLikeIgnoreCaseOrderByNameAsc("San%")).hasSize(2).containsSequence(sanDiego, sanFrancisco);
    }

    @Test
    public void testUpdateName() throws Exception {
        cityOptional = cityRepository.findByNameIgnoreCase(TestConstants.SAN_DIEGO);
        assertThat(cityOptional.isPresent()).isTrue();

        int numberOfAffectedRows = cityRepository.updateCityName(TestConstants.SAN_DIEGO + "- UPDATED", TestConstants.SAN_DIEGO);
        assertThat(numberOfAffectedRows).isEqualTo(1);
        assertThat(cityRepository.findByNameIgnoreCase(TestConstants.SAN_DIEGO + "- UPDATED").isPresent()).isTrue();
    }

    @After
    public void cleanup() throws Exception {
        cityRepository.deleteAll();
    }

}
