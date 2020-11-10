describe.only('release path', () => {

  before(function login() {
    cy.logout();
    cy.loginUser();
  })

  it('release path is shown', function () {
    cy.server();
    cy.route('GET', '/api/release-path/microservice/1').as('apiRequest');

    cy.visit('/dashboard/release-path/1');

    cy.wait('@apiRequest');
    cy.get('jhi-release-path > .p3 > .vis-network > canvas');
    cy.get('jhi-release-graph > .p3 > .vis-network > canvas');

    cy.percySnapshot('release-path', { widths: [1024] });
  })

})
