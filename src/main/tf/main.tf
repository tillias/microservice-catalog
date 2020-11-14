terraform {
    required_providers {
        google = {
            source = "hashicorp/google"
        }
    }
}

provider "google" {
    version = "3.5.0"

    # Generate JSON using https://console.cloud.google.com/apis/credentials/serviceaccountkey
    credentials = file("service-account.json")
    project = "compact-lacing-280811"
    zone = "us-central1-a"
}

resource "google_compute_instance" "vm_instance" {
    name = "microcatalog-instance"
    machine_type = "e2-medium"
    metadata_startup_script = file("startup.sh")

    tags = [
        "http-server",
        "https-server"]

    boot_disk {
        initialize_params {
            image = "cos-cloud/cos-77-12371-1096-0"
        }
    }

    network_interface {
        network = "default"
        access_config {}
    }
}

output "gce_public_ip" {
    value = element(concat(google_compute_instance.vm_instance.*.network_interface.0.access_config.0.nat_ip, list("")), 0)
}
