{ pkgs ? import <nixpkgs> { } }:
pkgs.mkShell {
  nativeBuildInputs = with pkgs.pkgsBuildHost; [ temurin-bin-17 ];
}

