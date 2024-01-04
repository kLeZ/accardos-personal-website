# Introduction

This is my personal website, in which I want to put some of the interesting things I've worked on in a while.

# Architecture

I wanted to do hands-on work in this architecture,
avoiding the boring "portfolio website" or "curriculum vitae website."

I wanted to do something different.

And that's it, something different, a DevOps inspired, IaC (Infrastructure as Code) inspired, microservice architecture.

A fun implementation of a boring personal website.

# Developer workflow

To migrate the version for the entire multi-module maven project,
we use the versions-plugin that helps in the boring task of replacing the version in all the modules.

The (basic) command to fire for this task is the following.

```shell
mvn versions:set -DnewVersion=<VERSION>
```
