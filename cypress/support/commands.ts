// add new command to the existing Cypress interface

declare namespace Cypress {
  interface Chainable {
    login: typeof login
    logout: typeof logout
    loginUser: typeof loginUser
    createMicroservice: typeof createMicroservice
    deleteMicroservice: typeof deleteMicroservice
    createDependency: typeof createDependency
    deleteDependency: typeof deleteDependency
  }
}

function getTokenWithoutQuotationMarks(): string {
  const token = window.sessionStorage.getItem('jhi-authenticationtoken');
  return token.slice(1, token.length - 1);
}

function getBearerHeader(): string {
  return "Bearer " + getTokenWithoutQuotationMarks();
}

function loginUser(): void {
  login('user', 'user');
}

function login(username: string, password: string): void {
  cy.request('POST', '/api/authenticate', {
    username: username,
    password: password
  })
    .its('body')
    .then((res) => {
      window.sessionStorage.setItem('jhi-authenticationtoken', '"' + res.id_token + '"');
    })
}

function logout(): void {
  window.sessionStorage.clear();
}

function createMicroservice(microservice: any): void {
  cy.request({
    url: '/api/microservices',
    body: microservice,
    method: 'POST',
    headers: {
      Authorization: getBearerHeader()
    }
  });
}

function deleteMicroservice(name: string): void {
  cy.request({
    url: '/api/microservices/by/name/' + name,
    method: 'DELETE',
    headers: {
      Authorization: getBearerHeader()
    }
  });
}

function createDependency(name: string, source: string, target: string): void {
  cy.request({
    url: '/api/dependencies/with/name',
    body: {
      dependencyName: name,
      sourceName: source,
      targetName: target
    },
    method: 'POST',
    headers: {
      Authorization: getBearerHeader()
    }
  });
}

function deleteDependency(name: string): void {
  cy.request({
    url: '/api/dependencies/by/name/' + name,
    method: 'DELETE',
    headers: {
      Authorization: getBearerHeader()
    }
  });
}


Cypress.Commands.add('login', (username, password) => login(username, password));
Cypress.Commands.add('loginUser', () => loginUser());
Cypress.Commands.add('logout', () => logout());
Cypress.Commands.add('createMicroservice', (body) => createMicroservice(body));
Cypress.Commands.add('deleteMicroservice', (name) => deleteMicroservice(name));
Cypress.Commands.add('createDependency', (name, source, target) => createDependency(name, source, target));
Cypress.Commands.add('deleteDependency', (name) => deleteDependency(name));
