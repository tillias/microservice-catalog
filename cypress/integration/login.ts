describe('login', () => {
  it('login', function () {
    cy.logout();

    cy.visit('/');

    cy.get('.alert:nth-child(1) > .alert-link').click();
    cy.get('#username').type('user');
    cy.get('#password').type('user');
    cy.get('.btn').click();
    cy.get('.form').submit();

    cy.get('#home-logged-message').should('have.text', 'You are logged in as user "user".');
  })
})
