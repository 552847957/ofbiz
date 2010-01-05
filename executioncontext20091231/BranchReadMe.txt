ExecutionContext and Security-Aware Artifacts Notes
---------------------------------------------------

2009-12-31: I put this text file in the branch as a means
of keeping anyone who is interested updated on the progress
of the branch.

This branch is an implementation of the Security-Aware Artifacts
design document -

http://cwiki.apache.org/confluence/display/OFBTECH/OFBiz+Security+Redesign

and it is a work in progress.

The ExecutionContext interface is
scattered across several components due to the cross-dependency
or circular-dependency issue. Cross-dependency is when Class
A references Class B, and Class B references Class A, and both
classes are in separate components. There is no way to get them
to compile. The problem is compounded in ExecutionContext because
it references 3 or 4 components.

The workaround I came up with was to have the lowest level methods
declared in the api component, then have each component extend
the interface and add their methods. It's not pretty, but it works.

This is where you can find the interfaces:

org.ofbiz.api.context.ExecutionContext
  org.ofbiz.entity.ExecutionContext
    org.ofbiz.service.ExecutionContext

When the cross-dependency issues are solved, all of the extended
interfaces will be consolidated into one.

The interface implementations can be found in the context component. 
  
The ultimate goal of ExecutionContext is to have all client code
get the contained objects from ExecutionContext only - instead of
getting them from the various classes now in use. This initial
implementation focuses more on the ExecutionContext's role as
a means of tracking the execution path - which is needed for the
security-aware artifacts.

The AuthorizationManager and AccessController interfaces are based
on the java.security.* classes, and they are intended to be
implementation-agnostic. OFBiz will have an implementation based
on the entity engine, but the goal is to be able to swap out that
implementation with another.

If you want to see the ExecutionContext and AccessController in
action, change the settings in api.properties. You will see info
messages in the console log.

I added a security-aware Freemarker transform. Template
sections can be controlled with:

<@ofbizSecurity permission="view" artifactId="thisTemplate">Some text</@ofbizSecurity>

If the user has permission to view the artifact, then "Some text"
will be rendered.

The Authorization Manager is mostly working. Filtering
EntityListIterator values is not implemented due to architectural
problems.

---------------------------------------------------

2010-01-05: Artifact paths now support substitution ("?")
and wildcard ("*") path elements.
This solves an issue that was discussed during the design - how
to grant access to a particular artifact regardless of the
execution path. You can see examples of their use in
framework/security/data/SecurityData.xml and
framework/example/data/ExampleSecurityData.xml.

The Example component has been converted to the new
security design.

The Execution Context seems to fulfill all needs so far, and it
works pretty well, so its API could be considered stable at
this time.
