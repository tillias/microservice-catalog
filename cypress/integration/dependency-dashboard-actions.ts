describe('dependency dashboard', () => {

  const firstMicroservice = 'TestService1';
  const secondMicroservice = 'TestService2';
  const editedMicroservice = firstMicroservice + '-Edited';
  const newDependency = firstMicroservice + ' -> ' + secondMicroservice;

  before(function login() {
    cy.logout();
    cy.loginUser();
  })

  function cleanup(): void {
    cy.deleteDependency(newDependency);

    cy.deleteMicroservice(firstMicroservice);
    cy.deleteMicroservice(secondMicroservice);
    cy.deleteMicroservice(editedMicroservice);
  }

  beforeEach(function () {
    cleanup();
  })

  after(function () {
    cleanup();
  })

  function createMicroserviceRequest(name: string): void {
    cy.createMicroservice({
      name: name,
      ciUrl: name,
      description: name,
      gitUrl: name,
      imageUrl: name,
      status: {
        id: 1,
      },
      swaggerUrl: name,
      team: {
        id: 1,
      }
    });
  }

  it('create microservice', function () {

    cy.visit('/dashboard/dependencies');

    cy.get(':nth-child(4) > .btn').click();

    cy.get('#field_name').click();
    cy.get('#field_name').type(firstMicroservice);
    cy.get('#field_description').click();
    cy.get('#field_description').type(firstMicroservice);
    cy.get('#field_imageUrl').click();
    cy.get('#field_imageUrl').type(firstMicroservice);
    cy.get('#field_swaggerUrl').click();
    cy.get('#field_swaggerUrl').type(firstMicroservice);
    cy.get('#field_gitUrl').click();
    cy.get('#field_gitUrl').type(firstMicroservice);
    cy.get('#field_ciUrl').click();
    cy.get('#field_ciUrl').type(firstMicroservice);
    cy.get('#field_team').select('1: Object');
    cy.get('#field_status').select('1: Object');
    cy.get('#save-entity > span').click();

    // wait till dependency-dashboard will load it's data
    cy.get('ngx-spinner').should('not.be.visible');
    cy.get('canvas').click();
    cy.get('canvas').click(311, 301, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', firstMicroservice);
  })

  it('delete microservice', function () {
    createMicroserviceRequest(firstMicroservice);

    cy.visit('/dashboard/dependencies');


    // wait till dependency-dashboard will load it's data
    cy.get('ngx-spinner').should('not.be.visible');
    cy.get('canvas').click();
    cy.get('canvas').click(311, 301, {force: true});

    cy.get(':nth-child(7) > .btn').click();
    cy.get('#jhi-confirm-delete-microservice').click();
  })

  it('edit microservice', function () {
    createMicroserviceRequest(firstMicroservice);

    cy.visit('/dashboard/dependencies');

    // wait till dependency-dashboard will load it's data
    cy.get('ngx-spinner').should('not.be.visible');
    cy.get('canvas').click();
    cy.get('canvas').click(311, 301, {force: true});

    cy.get(':nth-child(5) > .btn').click();
    cy.get('#field_name').clear();
    cy.get('#field_name').type(editedMicroservice);
    cy.get('#save-entity > span').click();

    // wait till dependency-dashboard will load it's data
    cy.get('ngx-spinner').should('not.be.visible');
    cy.get('canvas').click();
    cy.get('canvas').click(311, 301, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', editedMicroservice);
  })

  it('create dependency', function () {
    createMicroserviceRequest(firstMicroservice);
    createMicroserviceRequest(secondMicroservice);

    cy.visit('/dashboard/dependencies');

    cy.get(':nth-child(6) > .btn').click();

    cy.get(':nth-child(2) > :nth-child(2) > jhi-microservice-search > .d-flex > .form-control')
      .type(firstMicroservice);
    cy.get('.ngb-highlight').click();

    cy.get(':nth-child(3) > :nth-child(2) > jhi-microservice-search > .d-flex > .form-control')
      .type(secondMicroservice);
    cy.get('.ngb-highlight').click();

    cy.get('.btn-primary').click();

    // click on firstMicroservice and validate edge is added in edge legend
    cy.get('ngx-spinner').should('not.be.visible');
    cy.get('canvas').click();
    cy.get('canvas').click(331, 300, {force: true});
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > .text-primary > a').should('have.text', newDependency);
  })

  xit('delete dependency', function () {
    cy.visit('/dashboard/dependencies');

    // TODO
  })

  xit('create dependency with start selected', function () {

    cy.visit('/dashboard/dependencies');

    // TODO
  })

  xit('create dependency with start and end selected', function () {

    cy.visit('/dashboard/dependencies');

    // TODO
  })

  xit('create dependency with swap', function () {

    cy.visit('/dashboard/dependencies');

    // TODO
  })

})
