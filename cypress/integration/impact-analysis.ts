describe.only('impact analysis', () => {

  before(function login() {
    cy.logout();
    cy.loginUser();
  })

  it('impact analysis is shown', function () {
   cy.server();
    cy.route('GET', '/api/impact-analysis/microservice/4').as('apiRequest');

    cy.visit('/dashboard/impact-analysis/4');

    cy.wait('@apiRequest');
    cy.get('canvas');

    cy.percySnapshot('impact-analysis', { widths: [1024] });
  })

})
