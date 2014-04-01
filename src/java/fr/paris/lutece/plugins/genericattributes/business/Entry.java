/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.genericattributes.business;

import fr.paris.lutece.portal.service.util.AppLogService;

import java.io.Serializable;

import java.util.List;


/**
 *
 * class Entry
 *
 */
public class Entry implements Serializable, Cloneable
{
    private static final long serialVersionUID = 7623715927165156626L;

    //Other constants
    private int _nIdEntry;
    private String _strTitle;
    private String _strHelpMessage;
    private String _strComment;
    private boolean _bMandatory;
    private boolean _bFieldInLine;
    private IMapProvider _mapProvider;
    private int _nPosition;
    private int _nIdResource;
    private String _strResourceType;
    private EntryType _entryType;
    private Entry _entryParent;
    private List<Entry> _listEntryChildren;
    private List<Field> _listFields;
    private Field _fieldDepend;
    private int _nNumberConditionalQuestion;
    private boolean _nFirstInTheList;
    private boolean _nLastInTheList;
    private boolean _bConfirmField;
    private String _strConfirmFieldTitle;
    private boolean _bUnique;
    private GenericAttributeError _error;
    private String _strCSSClass;
    private String _strErrorMessage;

    /**
     * Get the list of children of this entry
     * @return the list of entry who are insert in the group
     */
    public List<Entry> getChildren(  )
    {
        return _listEntryChildren;
    }

    /**
     * Get the comment of this entry
     * @return the entry comment
     */
    public String getComment(  )
    {
        return _strComment;
    }

    /**
     * Get the type of the entry
     * @return the type of the entry
     */
    public EntryType getEntryType(  )
    {
        return _entryType;
    }

    /**
     * Get the list of fields of this entry
     * @return the list of field who are associate to the entry
     */
    public List<Field> getFields(  )
    {
        return _listFields;
    }

    /**
     * Get the help message of this entry
     * @return The help message of this entry
     */
    public String getHelpMessage(  )
    {
        return _strHelpMessage;
    }

    /**
     * Get the id of this entry
     * @return the id of entry
     */
    public int getIdEntry(  )
    {
        return _nIdEntry;
    }

    /**
     * @return parent entry if the entry is insert in a group
     */
    public Entry getParent(  )
    {
        return _entryParent;
    }

    /**
     * Get the position of the entry
     * @return position entry
     */
    public int getPosition(  )
    {
        return _nPosition;
    }

    /**
     * Get the title of this entry
     * @return The title of this entry
     */
    public String getTitle(  )
    {
        return _strTitle;
    }

    /**
     * Check if generated fields must be displayed in line
     * @return true if the field associate must be display in line
     */
    public boolean isFieldInLine(  )
    {
        return _bFieldInLine;
    }

    /**
     * Check if this entry is mandatory or not
     * @return true if the question is mandatory
     */
    public boolean isMandatory(  )
    {
        return _bMandatory;
    }

    /**
     * Set the list of entry who are insert in the group
     * @param children the list of entry
     */
    public void setChildren( List<Entry> children )
    {
        _listEntryChildren = children;
    }

    /**
     * Set entry comment
     * @param strComment entry comment
     */
    public void setComment( String strComment )
    {
        _strComment = strComment;
    }

    /**
     * Set the type of the entry
     * @param entryType the type of the entry
     */
    public void setEntryType( EntryType entryType )
    {
        _entryType = entryType;
    }

    /**
     * Set true if the field associate must be display in line
     * @param bFieldInLine true if the field associate must be display in line
     */
    public void setFieldInLine( boolean bFieldInLine )
    {
        _bFieldInLine = bFieldInLine;
    }

    /**
     * Set the list of field who are associate to the entry
     * @param fields the list of field
     */
    public void setFields( List<Field> fields )
    {
        _listFields = fields;
    }

    /**
     * Set the entry help message
     * @param strHelpMessage the entry help message
     */
    public void setHelpMessage( String strHelpMessage )
    {
        _strHelpMessage = strHelpMessage;
    }

    /**
     * Set the id of the entry
     * @param nIdEntry the id of the entry
     */
    public void setIdEntry( int nIdEntry )
    {
        _nIdEntry = nIdEntry;
    }

    /**
     * Set true if the question is mandatory
     * @param bMandatory true if the question is mandatory
     */
    public void setMandatory( boolean bMandatory )
    {
        _bMandatory = bMandatory;
    }

    /**
     * Set parent entry if the entry is insert in a group
     * @param parent parent entry
     */
    public void setParent( Entry parent )
    {
        _entryParent = parent;
    }

    /**
     * Set position entry
     * @param position position entry
     */
    public void setPosition( int position )
    {
        _nPosition = position;
    }

    /**
     * Set title entry
     * @param strTitle title
     */
    public void setTitle( String strTitle )
    {
        _strTitle = strTitle;
    }

    /**
     * Get the id of the resource associated with this entry
     * @return The id of the resource associated with this entry
     */
    public int getIdResource(  )
    {
        return _nIdResource;
    }

    /**
     * Set the id of the resource associated with this entry
     * @param nIdResource The id of the resource associated with this entry
     */
    public void setIdResource( int nIdResource )
    {
        this._nIdResource = nIdResource;
    }

    /**
     * Get the type of the resource associated with this entry
     * @return The type of the resource associated with this entry
     */
    public String getResourceType(  )
    {
        return _strResourceType;
    }

    /**
     * Set the type of the resource associated with this entry
     * @param strResourceType The type of the resource associated with this
     *            entry
     */
    public void setResourceType( String strResourceType )
    {
        this._strResourceType = strResourceType;
    }

    /**
     * Get the field depend of this entry
     * @return the field if the entry is a conditional question
     */
    public Field getFieldDepend(  )
    {
        return _fieldDepend;
    }

    /**
     * Set the field if the entry is a conditional question
     * @param depend depend the field if the entry is a conditional question
     */
    public void setFieldDepend( Field depend )
    {
        _fieldDepend = depend;
    }

    /**
     * Get the number of conditional questions associated with the
     * entry
     * @return the number of conditional questions associated with the
     *         entry
     */
    public int getNumberConditionalQuestion(  )
    {
        return _nNumberConditionalQuestion;
    }

    /**
     * Set the number of conditional questions who are associated with the entry
     * @param numberConditionalQuestion the number of conditional questions
     *            which are associated with the entry
     *
     */
    public void setNumberConditionalQuestion( int numberConditionalQuestion )
    {
        _nNumberConditionalQuestion = numberConditionalQuestion;
    }

    /**
     * Check if the entry is the last entry of a group
     * @return true if the entry is the last entry of a group or the list of
     *         entry
     */
    public boolean isLastInTheList(  )
    {
        return _nLastInTheList;
    }

    /**
     * Set true if the entry is the last entry of a group or the list of entry
     * @param lastInTheList true if the entry is the last entry of a group or
     *            the list of entry
     */
    public void setLastInTheList( boolean lastInTheList )
    {
        _nLastInTheList = lastInTheList;
    }

    /**
     * Check if the entry is the first entry of a group
     * @return true if the entry is the first entry of a group or the list of
     *         entry
     */
    public boolean isFirstInTheList(  )
    {
        return _nFirstInTheList;
    }

    /**
     * Set true if the entry is the first entry of a group or the list of entry
     * @param firstInTheList true if the entry is the last entry of a group or
     *            the list of entry
     */
    public void setFirstInTheList( boolean firstInTheList )
    {
        _nFirstInTheList = firstInTheList;
    }

    /**
     * Set true if the question must be confirmed by a duplicated field
     * @param bConfirmField mandatory true if the question must be confirmed by
     *            a duplicated field
     */
    public void setConfirmField( boolean bConfirmField )
    {
        this._bConfirmField = bConfirmField;
    }

    /**
     * Check if this entry must be confirmed by a duplicated field
     * @return true if the entry must be confirmed by a duplicated field
     */
    public boolean isConfirmField(  )
    {
        return _bConfirmField;
    }

    /**
     * Set the title of the confirmation field
     * @param strConfirmFieldTitle The title of the confirmation field
     */
    public void setConfirmFieldTitle( String strConfirmFieldTitle )
    {
        this._strConfirmFieldTitle = strConfirmFieldTitle;
    }

    /**
     * Get the title of the confirmation field
     * @return The title of the confirmation field
     */
    public String getConfirmFieldTitle(  )
    {
        return _strConfirmFieldTitle;
    }

    /**
     * Set to true if the value of the response to this question must be unique
     * @param bUnique true if the value of the response to this question must be
     *            unique, false otherwise
     */
    public void setUnique( boolean bUnique )
    {
        this._bUnique = bUnique;
    }

    /**
     * Check if the value of the response must be unique
     * @return true if the value of the response to this question must be unique
     */
    public boolean isUnique(  )
    {
        return _bUnique;
    }

    /**
     * Get the selected map provider
     * @see IMapProvider
     * @return the select map provider
     */
    public IMapProvider getMapProvider(  )
    {
        return _mapProvider;
    }

    /**
     * Sets the map provider
     * @param mapProvider the map provider
     */
    public void setMapProvider( IMapProvider mapProvider )
    {
        _mapProvider = mapProvider;
    }

    /**
     * Get the error associated to the entry
     * @return the error
     */
    public GenericAttributeError getError(  )
    {
        return _error;
    }

    /**
     * Set the error associated to the entry
     * @param error the error
     */
    public void setError( GenericAttributeError error )
    {
        _error = error;
    }

    /**
     * Set the CSS class of the generated fields
     * @param strCSSClass The CSS class to set
     */
    public void setCSSClass( String strCSSClass )
    {
        this._strCSSClass = strCSSClass;
    }

    /**
     * Get the CSS class of the generated fields
     * @return The CSS class
     */
    public String getCSSClass(  )
    {
        return _strCSSClass;
    }

    /**
     * Get the error message associated with this entry. This error message
     * should be used by the right entry type. For example, EntryTypeCheckBox
     * use it as a message to indicates that this field is mandatory.
     * @return The error message of this entry
     */
    public String getErrorMessage(  )
    {
        return _strErrorMessage;
    }

    /**
     * Set the error message associated with this entry. This error message
     * should be used by the right entry type. For example, EntryTypeCheckBox
     * use it as a message to indicates that this field is mandatory.
     * @param strErrorMessage The error message of this entry
     */
    public void setErrorMessage( String strErrorMessage )
    {
        this._strErrorMessage = strErrorMessage;
    }

    /**
     * Creates and returns a copy of this object.
     * @return a clone of this instance.
     * @see java.lang.Object#clone()
     */
    @Override
    public Object clone(  )
    {
        try
        {
            return super.clone(  );
        }
        catch ( CloneNotSupportedException e )
        {
            AppLogService.error( e.getMessage(  ), e );

            return new Entry(  );
        }
    }
}