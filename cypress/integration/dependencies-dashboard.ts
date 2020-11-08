describe('microservice-catalog e2e', () => {
  it('check dependency dashboard selection', function () {
    cy.visit('/');
    cy.get('.alert:nth-child(1) > .alert-link').click();
    cy.get('#username').type('user');
    cy.get('#password').type('user');
    cy.get('.btn').click();
    cy.get('.form').submit();
    cy.get('#dashboard > span > span').click();

    // wait till dependency-dashboard will load it's data
    cy.get('ngx-spinner').should('not.be.visible');

    cy.get('canvas').click();
    cy.get('canvas').click(249, 320, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 1');
    cy.get('canvas').click(209, 135, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 4');
  })
})
