package com.robocubs4205.cubscout.model;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(BlockJUnit4ClassRunner.class)
public class DistrictRepositoryImplTests {
    private PersistenceManagerFactory pmf = realPmf();

    private PersistenceManagerFactory realPmf() {
        return JDOHelper.getPersistenceManagerFactory("PU");
    }

    @Before
    public void clearGames() {
        pmf.getPersistenceManager().newQuery(District.class).deletePersistentAll();
    }

    @Test
    public void findById_NonTX_NoGames_Throws() {
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);
        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            districtRepository.find("foo");
        });
    }

    @Test
    public void findById_NonTX_GameDoesNotExist_Throws() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            district = pm.makePersistent(district);
        }
        String code = district.getCode();
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        assertThatExceptionOfType(JDOObjectNotFoundException.class).isThrownBy(() -> {
            districtRepository.find(code + 1);
        });
    }

    @Test
    public void findByCode_NonTX_GameExists_returnValueIsDetached() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            district = pm.makePersistent(district);
        }
        String code = district.getCode();
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        District foundDistrict = districtRepository.find(code);

        assertThat(foundDistrict).matches(JDOHelper::isDetached);
    }

    @Test
    public void findAll_NonTx_NoDistricts_ResultSetIsEmpty() {
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);
        assertThat(districtRepository.findAll()).isEmpty();
    }

    @Test
    public void findAll_NonTx_OneGame_ResultSizeIs1() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        try (PersistenceManager pm = pmf.getPersistenceManager()) {
            district = pm.makePersistent(district);
        }
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        assertThat(districtRepository.findAll()).size().isEqualTo(1);
    }

    @Test
    public void save_NonTX_ReturnValueIsDetached() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        assertThat(districtRepository.save(district)).matches(JDOHelper::isDetached);
    }

    @Test
    public void save_NonTX_ResultIsInFindAll() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        district = districtRepository.save(district);

        assertThat(districtRepository.findAll()).containsExactly(district);
    }

    @Test
    public void save_NonTX_multipleSaves_ResultsAreInFindAll() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        District district2 = new District();
        district2.setName("bar");
        district2.setCode("foo");
        District district3 = new District();
        district3.setName("foooooo");
        district3.setCode("baz");
        District district4 = new District();
        district4.setName("a");
        district4.setCode("b");
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        district = districtRepository.save(district);
        district2 = districtRepository.save(district2);
        district3 = districtRepository.save(district3);
        district4 = districtRepository.save(district4);

        assertThat(districtRepository.findAll())
            .containsExactlyInAnyOrder(district,district2,district3,district4);
    }

    @Test
    public void delete_NonTX_OneDistrict_FindAllIsEmpty() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);
        district = districtRepository.save(district);

        districtRepository.delete(district);

        assertThat(districtRepository.findAll()).isEmpty();
    }

    @Test
    public void delete_NonTX_OneDeletion_MultipleDistrict_DeletedNotInFindAll() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        District district2 = new District();
        district2.setName("bar");
        district2.setCode("foo");
        District district3 = new District();
        district3.setName("foooooo");
        district3.setCode("baz");
        District district4 = new District();
        district4.setName("a");
        district4.setCode("b");
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        district = districtRepository.save(district);
        district2 = districtRepository.save(district2);
        district3 = districtRepository.save(district3);
        district4 = districtRepository.save(district4);

        districtRepository.delete(district2);

        assertThat(districtRepository.findAll())
            .containsExactlyInAnyOrder(district,district3,district4);
    }

    @Test
    public void delete_NonTX_TwoDeletions_MultipleDistrict_DeletedNotInFindAll() {
        District district = new District();
        district.setName("foo");
        district.setCode("bar");
        District district2 = new District();
        district2.setName("bar");
        district2.setCode("foo");
        District district3 = new District();
        district3.setName("foooooo");
        district3.setCode("baz");
        District district4 = new District();
        district4.setName("a");
        district4.setCode("b");
        DistrictRepository districtRepository = new DistrictRepositoryImpl(pmf);

        district = districtRepository.save(district);
        district2 = districtRepository.save(district2);
        district3 = districtRepository.save(district3);
        district4 = districtRepository.save(district4);

        districtRepository.delete(district2);
        districtRepository.delete(district3);

        assertThat(districtRepository.findAll())
            .containsExactlyInAnyOrder(district,district4);
    }
}
