package me.fit.service;

import java.util.List;

import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import me.fit.model.rest.client.Country;

@Dependent
public class CountryService {
	
	@Inject
	private EntityManager em;

	@Transactional
	public void saveCountries(List<Country> countries) {
		
		List<Country> savedCountries = getAllCountries();
		
		countries.removeAll(savedCountries);
		
		for (Country country : countries) {
			em.merge(country);
		}
	}
	
	@Transactional
	public List<Country> getAllCountries(){
		return em.createNamedQuery(Country.GET_ALL, Country.class).getResultList();
	}
}
