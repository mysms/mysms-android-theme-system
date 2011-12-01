#!/usr/bin/perl

my $manifest = `cat AndroidManifest.xml`;
if ($manifest =~ /versionName="([\d\.]+)"/) {
  my $version = $1;
  
  print STDOUT "build version: $1\n";
  
  system("mkdir -p release");
  
  system("ant release");
  system("mv bin/mysms-theme-system-release.apk release/mysms-theme-system-$version.apk");

  for my $store ((
    "amazon"
    )) { 
    system("ant -Dconfig=$store release");
    system("mv bin/mysms-theme-system-release.apk release/mysms-theme-system-$store-$version.apk");
  }
}