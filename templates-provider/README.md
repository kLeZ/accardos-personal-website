# Templates Provider microservice

This microservice is a simple REST API interface to te redis key-value store that contains the templates, normally used within a web app UI as page fragments.

## Setup

> WARNING: Instructions specifically stated for OpenSuSE Linux system distribution, may vary on other distro

1. Install Redis Database
	> On OpenSuSE this is as simple as:
```sudo zypper install redis```
2. Configure a store
	> A configuration must be created (generally copying the .sample file already included from the distro package) in the folder `/etc/redis`
3. Configure a password through the parameter `requirepass` inside redis configuration file
	> Make sure that the password is copied inside the `templates.yml` configuration file of this microservice placed inside the `src/main/resources` folder
