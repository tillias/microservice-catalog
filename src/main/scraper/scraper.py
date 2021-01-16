"""
This scraper attempts downloading and importing microservice descriptors into microservice-catalog using urls
defined in .repos. Each line in this file should contain url for microservice descriptor.

Please adjust base_url pointing to your running microservice-catalog instance
"""

import requests

base_url = "http://localhost:8080"


def authenticate():
    import_user = "import"
    import_password = "import"
    auth_url = base_url + "/api/authenticate"
    r = requests.post(auth_url, json={'username': import_user, 'password': import_password, 'rememberMe': 'false'})
    if r.status_code == requests.codes.ok:
        return r.json()['id_token']
    else:
        raise RuntimeError("Authentication failed")


def import_microservice(descriptor, bearer):
    import_url = base_url + "/api/import"
    r = requests.post(import_url, json=descriptor, headers={'Authorization': 'Bearer ' + bearer})
    if r.status_code == requests.codes.ok:
        print("Microservice has been imported")
    else:
        print("Error importing microservice: " + r.text)


def download_microservice_descriptors():
    descriptors = []
    with open('.repos') as descriptors_locations:
        for line in descriptors_locations.read().splitlines():
            r = requests.get(line)
            if r.status_code == requests.codes.ok:
                descriptors.append(r.json())
            else:
                print("Error downloading microservice descriptor from: " + line)
    return descriptors


def import_microservices():
    bearer = authenticate()
    for descriptor in download_microservice_descriptors():
        import_microservice(descriptor, bearer)


import_microservices()
