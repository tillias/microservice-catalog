describe('dependency dashboard', () => {

  before(function login() {
    cy.logout();
    cy.loginUser();
  })


  it('dependency graph is built', function () {

    cy.visit('/');

    cy.get('#dashboard > span > span').click();

    // wait till dependency-dashboard will load it's data
    cy.get('ngx-spinner').should('not.be.visible');

    cy.get('canvas').click();

    cy.get('canvas').click(244, 327, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 1');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '1->2');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '1->8');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(3) > a').should('have.text', '9->1');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(4) > a').should('have.text', 'Some old service->1');

    cy.get('canvas').click(203, 237, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 2');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '1->2');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '2->4');

    cy.get('canvas').click(41, 196, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 3');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > .text-primary > a').should('have.text', '3->5');

    cy.get('canvas').click(209, 135, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 4');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '2->4');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '4->8');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(3) > a').should('have.text', '6->4');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(4) > a').should('have.text', '4->5');
    cy.get(':nth-child(2) > :nth-child(5) > a').should('have.text', '4->7');
    cy.get(':nth-child(6) > a').should('have.text', '4->10');

    cy.get('canvas').click(144, 204, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 5');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '4->5');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '10->5');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(3) > a').should('have.text', '3->5');

    cy.get('canvas').click(301, 81, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 6');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > .text-primary > a').should('have.text', '6->4');

    cy.get('canvas').click(191, 34, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 7');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '4->7');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '7->10');

    cy.get('canvas').click(260, 219, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 8');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '1->8');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '4->8');

    cy.get('canvas').click(107, 100, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 10');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(1) > a').should('have.text', '4->10');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(2) > a').should('have.text', '7->10');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > :nth-child(3) > a').should('have.text', '10->5');

    cy.get('canvas').click(340, 205, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 20');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > .text-primary > a').should('have.text', '20->21');

    cy.get('canvas').click(412, 122, {force: true});
    cy.get('jhi-vertex-legend a').should('have.text', 'Service 21');
    cy.get('jhi-edge-legend.ng-star-inserted > :nth-child(2) > .text-primary > a').should('have.text', '20->21');
  })
})
