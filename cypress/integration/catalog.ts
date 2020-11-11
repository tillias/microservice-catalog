describe.only('catalog and search', () => {

  before(function login() {
    cy.logout();
    cy.loginUser();
  })


  it('autocomplete search works', function () {
    cy.visit('/');

    cy.get('[autocapitalize="off"]').type('Service 1');
    cy.get('#ngb-typeahead-0-0').click();

    cy.get('.card-body').should('have.length', 1);
  })

  it('advanced filter case insensitive search by name', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(2) > .form-control').click();
    cy.get(':nth-child(2) > .form-control').type('old service');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Some old service');
  })

  it('advanced filter case insensitive search by name not found', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(2) > .form-control').click();
    cy.get(':nth-child(2) > .form-control').type('Something to be not found');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 0);
  })

  it('advanced filter case insensitive search by name', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(2) > .form-control').click();
    cy.get(':nth-child(2) > .form-control').type('Some old');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Some old service');
  })

  it('advanced filter case sensitive search by name not found', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();

    cy.get('#customSwitch1').click({force: true});

    cy.get('.row:nth-child(2) > .form-control').click();
    cy.get(':nth-child(2) > .form-control').type('Old service');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 0);
  })

  it('advanced filter case insensitive search by description', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(3) > .form-control').click();
    cy.get(':nth-child(3) > .form-control').type('custom old service description');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('h6').contains('This is custom old service description');

  })

  it('advanced filter case insensitive search by description not found', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(3) > .form-control').click();
    cy.get(':nth-child(3) > .form-control').type('Something not to be found');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 0);

  })

  it('advanced filter case sensitive search by description not found', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();

    cy.get('#customSwitch1').click({force: true});

    cy.get('.row:nth-child(3) > .form-control').click();
    cy.get(':nth-child(3) > .form-control').type('Custom old service description');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 0);
  })

  it('advanced filter case sensitive search by description', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();

    cy.get('#customSwitch1').click({force: true});

    cy.get('.row:nth-child(3) > .form-control').click();
    cy.get(':nth-child(3) > .form-control').type('This is custom');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('h6').contains('This is custom old service description');
  })

  it('advanced filter case insensitive search by swagger url', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(4) > .form-control').click();
    cy.get(':nth-child(4) > .form-control').type('some-old-service');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Some old service');
    cy.get('div > :nth-child(5) > a').should('have.attr', 'href');
    cy.get('div > :nth-child(5) > a').attribute('href').should('eq', 'some-old-service/swagger');
  })

  it('advanced filter case insensitive search by git url', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(5) > .form-control').click();
    cy.get(':nth-child(5) > .form-control').type('some-old-service');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Some old service');
    cy.get('div > :nth-child(1) > a').should('have.attr', 'href');
    cy.get('div > :nth-child(1) > a').attribute('href').should('eq', 'some-old-service/repo.git');
  })

  it('advanced filter search by team', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(6) > .custom-select').select('3: Object');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Some old service');
    cy.get('.font-weight-bold').should('have.text', 'Team: Team Three');
  })

  it('advanced filter search by status', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(7) > .custom-select').select('4: Object');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Some old service');
    cy.get('.badge').should('have.text', 'Draft');
  })

  it('advanced filter search by all attributes', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(6) > .custom-select').select('1: Object');
    cy.get('.row:nth-child(7) > .custom-select').select('1: Object');

    cy.get('.row:nth-child(5) > .form-control').click();
    cy.get(':nth-child(5) > .form-control').type('lime'); // git
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body')

    cy.get('.card-body').should('have.length', 1);
    cy.get('.h3').should('have.text', 'Service 1');
    cy.get('.badge').should('have.text', 'Released');
    cy.get('div > :nth-child(1) > a').should('have.attr', 'href');
    cy.get('div > :nth-child(1) > a').attribute('href').should('eq', 'lime Denmark');
  })

  it('clear filters', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(7) > .custom-select').select('4: Object');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);

    cy.get('[jhitranslate="microcatalogApp.microservice.search.group.clear"]').click();
    cy.get('.card-body').should('have.length', 13);
  })

  it('details screen is accessible', function () {
    cy.visit('/');

    cy.get('.btn-secondary').click();
    cy.get('.row:nth-child(7) > .custom-select').select('4: Object');
    cy.get('.btn-primary:nth-child(1)').click();

    cy.get('.card-body').should('have.length', 1);

    cy.get('.text-primary > a').click();

    cy.get('h2').should('have.text', 'Microservice 11');
  })
})

