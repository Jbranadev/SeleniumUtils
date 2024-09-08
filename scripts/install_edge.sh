#!/bin/bash
set -e

# Añade la clave del repositorio de Microsoft
wget https://packages.microsoft.com/keys/microsoft.asc
sudo apt-key add microsoft.asc

# Añade el repositorio de Microsoft Edge
sudo sh -c 'echo "deb [arch=amd64] https://packages.microsoft.com/repos/edge stable main" > /etc/apt/sources.list.d/microsoft-edge-dev.list'

# Actualiza la lista de paquetes
sudo apt update

# Instala Microsoft Edge
sudo apt install -y microsoft-edge-stable

# Limpia el archivo de clave
rm microsoft.asc
