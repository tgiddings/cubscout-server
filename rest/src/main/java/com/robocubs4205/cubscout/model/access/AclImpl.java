package com.robocubs4205.cubscout.model.access;

import org.springframework.security.acls.model.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AclImpl implements MutableAcl {
    @Id
    private long id;
    @OneToMany(mappedBy = "acl")
    @OrderColumn
    private List<AceImpl> aces = new ArrayList<>();
    @ManyToOne
    private SidImpl owner;
    @OneToOne
    private AclImpl parent;
    private boolean isEntriesInheriting;
    @ManyToOne
    private ObjectIdentityImpl objectIdentity;

    @Override
    public List<AccessControlEntry> getEntries() {
        return new ArrayList<>(aces);
    }

    @Override
    public ObjectIdentity getObjectIdentity() {
        return objectIdentity;
    }

    public void setObjectIdentity(ObjectIdentityImpl objectIdentity) {
        this.objectIdentity = objectIdentity;
    }

    @Override
    public Sid getOwner() {
        return owner;
    }

    public void setOwner(SidImpl owner) {
        this.owner = owner;
    }

    @Override
    public void setOwner(Sid newOwner) {
        setOwner((SidImpl) newOwner);
    }

    @Override
    public Acl getParentAcl() {
        return parent;
    }

    public void setParent(AclImpl parent) {
        this.parent = parent;
    }

    @Override
    public boolean isEntriesInheriting() {
        return isEntriesInheriting;
    }

    @Override
    public void setEntriesInheriting(boolean entriesInheriting) {
        isEntriesInheriting = entriesInheriting;
    }

    @Override
    public boolean isGranted(List<Permission> permissions, List<Sid> sids,
                             boolean administrativeMode) throws NotFoundException, UnloadedSidException {
        return permissions.stream().allMatch(permission -> {
            return sids.stream().anyMatch(sid -> {
                return aces.stream()
                           .filter(ace -> ace.getPermission().getMask() == permission.getMask())
                           .filter(ace -> ace.getSid().equals(sid))
                           .allMatch(AceImpl::isGranting);
            });
        });
    }

    @Override
    public boolean isSidLoaded(List<Sid> sids) {
        return true;
    }

    @Override
    public void deleteAce(int aceIndex) throws NotFoundException {
        aces.remove(aceIndex);
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void insertAce(int atIndexLocation, Permission permission, Sid sid,
                          boolean granting) throws NotFoundException {
        AceImpl ace = new AceImpl();
        ace.setPermission((PermissionImpl) permission);
        ace.setSid((SidImpl) sid);
        ace.setAcl(this);
        ace.setGranting(granting);
        aces.add(atIndexLocation, ace);
    }

    @Override
    public void setParent(Acl newParent) {
        this.parent = (AclImpl) newParent;
    }

    @Override
    public void updateAce(int aceIndex, Permission permission) throws NotFoundException {
        this.aces.get(aceIndex).setPermission((PermissionImpl) permission);
    }
}
